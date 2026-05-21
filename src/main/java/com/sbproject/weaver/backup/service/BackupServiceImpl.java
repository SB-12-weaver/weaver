package com.sbproject.weaver.backup.service;

import com.sbproject.weaver.backup.dto.BackupDto;
import com.sbproject.weaver.backup.entity.BackupEntity;
import com.sbproject.weaver.backup.entity.BackupStatus;
import com.sbproject.weaver.backup.mapper.BackupMapper;
import com.sbproject.weaver.backup.repository.BackupRepository;
import com.sbproject.weaver.common.dto.CursorPageResponse;
import com.sbproject.weaver.employee.dto.EmployeeBackupRow;
import com.sbproject.weaver.employee.entity.Employee;
import com.sbproject.weaver.employee.repository.EmployeeRepository;
import com.sbproject.weaver.file.entity.FileEntity;
import com.sbproject.weaver.file.service.FileService;
import com.sbproject.weaver.file.type.FilePurpose;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private static final int BACKUP_BATCH_SIZE = 20000;

    private final BackupRepository backupRepository;
    private final EmployeeRepository employeeRepository;
    private final FileService fileService;
    private final BackupMapper backupMapper;

    @Override
    @Transactional
    public BackupDto runBackup(String worker) {
        long totalStartTime = System.currentTimeMillis();

        BackupEntity backup = BackupEntity.builder()
                .worker(worker)
                .startedAt(Instant.now())
                .build();

        Instant lastSuccessTime = backupRepository.findFirstByStatusOrderByStartedAtDesc(BackupStatus.COMPLETED)
                .map(BackupEntity::getEndedAt)
                .orElse(null);

        boolean needsBackup = lastSuccessTime == null
                || employeeRepository.existsByUpdatedAtAfter(lastSuccessTime);

        if (!needsBackup) {
            backup.skip(Instant.now());
            BackupEntity savedBackup = backupRepository.save(backup);

            log.info("[Backup Performance] result=SKIPPED, elapsed={}ms",
                    System.currentTimeMillis() - totalStartTime
            );

            return backupMapper.toBackupDto(savedBackup);
        }

        Path tempCsvPath = null;

        try {
            long csvStartTime = System.currentTimeMillis();

            CsvFileResult csvFileResult = createCsvFile();
            tempCsvPath = csvFileResult.path();

            long csvElapsed = System.currentTimeMillis() - csvStartTime;

            String fileName = String.format(
                    "employee_backup_%s_%s.csv",
                    backup.getId().toString().substring(0, 8),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            );

            FileEntity savedFile = fileService.saveFile(
                    fileName,
                    "text/csv",
                    tempCsvPath,
                    FilePurpose.BACKUP_CSV
            );

            backup.complete(Instant.now(), savedFile);

            BackupEntity savedBackup = backupRepository.save(backup);

            long totalElapsed = System.currentTimeMillis() - totalStartTime;

            log.info(
                    "[Backup Performance] result=COMPLETED, employees={}, batchSize={}, csvSize={}MB, csvElapsed={}ms, totalElapsed={}ms",
                    csvFileResult.employeeCount(),
                    BACKUP_BATCH_SIZE,
                    csvFileResult.sizeBytes() / 1024 / 1024,
                    csvElapsed,
                    totalElapsed
            );

            return backupMapper.toBackupDto(savedBackup);

        } catch (Exception e) {
            log.error("백업 작업 중 오류 발생", e);

            byte[] errorLog = e.toString().getBytes(StandardCharsets.UTF_8);

            String fileName = String.format(
                    "backup_error_%s_%s.log",
                    backup.getId().toString().substring(0, 8),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            );

            FileEntity errorFile = fileService.saveBytes(
                    fileName,
                    "text/plain",
                    errorLog,
                    FilePurpose.BACKUP_LOG
            );

            backup.fail(Instant.now(), errorFile);

            BackupEntity savedBackup = backupRepository.save(backup);

            log.info("[Backup Performance] result=FAILED, elapsed={}ms",
                    System.currentTimeMillis() - totalStartTime
            );

            return backupMapper.toBackupDto(savedBackup);

        } finally {
            deleteTempFile(tempCsvPath);
        }
    }

    @Transactional(readOnly = true)
    public BackupDto getLatestBackup(BackupStatus status) {
        BackupEntity backup = backupRepository.findFirstByStatusOrderByStartedAtDesc(status)
                .orElseThrow(() -> new IllegalArgumentException("백업 이력이 없습니다. status=" + status));

        return backupMapper.toBackupDto(backup);
    }

    @Transactional(readOnly = true)
    public CursorPageResponse<BackupDto> findBackups(
            String worker,
            BackupStatus status,
            String from,
            String to,
            String cursor,
            String idAfter,
            int size,
            String sortField,
            String sortDirection
    ) {
        Instant fromInstant = parseInstantOrNull(from);
        Instant toInstant = parseInstantOrNull(to);

        String activeCursor = cursor != null && !cursor.isBlank()
                ? cursor
                : idAfter;

        int limit = size + 1;

        List<BackupDto> fetched = backupRepository.findBackups(
                worker,
                status,
                fromInstant,
                toInstant,
                activeCursor,
                sortDirection,
                sortField,
                limit
        );

        boolean hasNext = fetched.size() > size;

        List<BackupDto> content = hasNext
                ? fetched.subList(0, size)
                : fetched;

        String nextCursor = null;

        if (hasNext && !content.isEmpty()) {
            BackupDto last = content.get(content.size() - 1);
            nextCursor = last.getId().toString();
        }

        long totalElements = backupRepository.countBackups(
                worker,
                status,
                fromInstant,
                toInstant
        );

        return CursorPageResponse.<BackupDto>builder()
                .content(content)
                .nextCursor(nextCursor)
                .nextIdAfter(null)
                .size(size)
                .totalElements(totalElements)
                .hasNext(hasNext)
                .build();
    }

    private CsvFileResult createCsvFile() {
        Path tempCsvPath;

        try {
            tempCsvPath = Files.createTempFile("employee_backup_", ".csv");
        } catch (IOException e) {
            throw new RuntimeException("임시 CSV 파일 생성 실패", e);
        }

        long processedCount = 0;
        int offset = 0;

        try (
                BufferedWriter writer = Files.newBufferedWriter(tempCsvPath, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                        .setHeader(
                                "id",
                                "employeeNumber",
                                "name",
                                "email",
                                "departmentId",
                                "position",
                                "hireDate",
                                "status"
                        )
                        .build())
        ) {
            Slice<Employee> page;

            while (true) {
                List<EmployeeBackupRow> rows = employeeRepository.findBackupRows(offset, BACKUP_BATCH_SIZE);

                if (rows.isEmpty()) {
                    break;
                }

                for (EmployeeBackupRow row : rows) {
                    csvPrinter.printRecord(
                            row.getId(),
                            row.getEmployeeNumber(),
                            row.getName(),
                            row.getEmail(),
                            row.getDepartmentId(),
                            row.getPosition(),
                            row.getHireDate(),
                            row.getStatus()
                    );
                }

                processedCount += rows.size();

                if (processedCount % 50000 == 0) {
                    log.info("[Backup CSV] processed employees={}", processedCount);
                }

                if (rows.size() < BACKUP_BATCH_SIZE) {
                    break;
                }

                offset += BACKUP_BATCH_SIZE;
            }

            long sizeBytes = Files.size(tempCsvPath);

            return new CsvFileResult(tempCsvPath, processedCount, sizeBytes);

        } catch (IOException e) {
            deleteTempFile(tempCsvPath);
            throw new RuntimeException("CSV 파일 생성 실패", e);
        }
    }

    private void deleteTempFile(Path tempCsvPath) {
        if (tempCsvPath == null) {
            return;
        }

        try {
            Files.deleteIfExists(tempCsvPath);
        } catch (IOException e) {
            log.warn("임시 CSV 파일 삭제 실패: {}", tempCsvPath, e);
        }
    }

    private Instant parseInstantOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Instant.parse(value);
    }

    private record CsvFileResult(
            Path path,
            long employeeCount,
            long sizeBytes
    ) {
    }
}