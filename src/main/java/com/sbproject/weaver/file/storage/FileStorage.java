package com.sbproject.weaver.file.storage;

import com.sbproject.weaver.config.FileConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class FileStorage {

    private final FileConfig fileConfig;

    public void save(String storagePath, byte[] bytes) {
        Path targetPath = fileConfig.getUploadDir().resolve(storagePath);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, bytes);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장을 실패하였습니다. : " + storagePath, e);
        }
    }

    // 대용량 파일 저장용: 전체 파일을 byte[]로 메모리에 올리지 않고 파일 경로 기준으로 저장
    public void save(String storagePath, Path sourcePath) {
        Path targetPath = fileConfig.getUploadDir().resolve(storagePath);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장을 실패하였습니다. : " + storagePath, e);
        }
    }

    public byte[] read(String storagePath) {
        Path targetPath = fileConfig.getUploadDir().resolve(storagePath);

        try {
            return Files.readAllBytes(targetPath);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기를 실패하였습니다. : " + storagePath, e);
        }
    }

    public void delete(String storagePath) {
        Path targetPath = fileConfig.getUploadDir().resolve(storagePath);

        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제를 실패하였습니다. : " + storagePath, e);
        }
    }
}