package com.sbproject.weaver.changelog.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sbproject.weaver.changelog.dto.ChangeLogSearchRequest;
import com.sbproject.weaver.changelog.entity.ChangeLogType;
import com.sbproject.weaver.changelog.entity.EmployeeChangeLog;
import com.sbproject.weaver.changelog.entity.QEmployeeChangeDiff;
import com.sbproject.weaver.changelog.entity.QEmployeeChangeLog;
import com.sbproject.weaver.changelog.repository.ChangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChangeLogRepositoryImpl implements ChangeLogRepository {
    private final JPAQueryFactory queryFactory;

    private static final QEmployeeChangeDiff diff = QEmployeeChangeDiff.employeeChangeDiff;
    private static final QEmployeeChangeLog log = QEmployeeChangeLog.employeeChangeLog;

    private static final QEmployeeChangeLog lodId = new QEmployeeChangeLog("log");

    @Override
    public Slice<EmployeeChangeLog> search(String cursor, int size, ChangeLogSearchRequest search, ChangeLogType type) {
        BooleanBuilder where = new BooleanBuilder();

        if (cursor != null) {
            where.and(log.id.gt(UUID.fromString(cursor)));
        }

        if (search.getEmployeeNumber() != null && !search.getEmployeeNumber().isBlank()) {
            where.and(log.employeeNumber.containsIgnoreCase(search.getEmployeeNumber()));
        }

        if (type != null) {
            where.and(log.type.eq(type));
        }
        if (search.getMemo() != null && !search.getMemo().isBlank()) {
            where.and(log.memo.containsIgnoreCase(search.getMemo()));
        }

        if (search.getIpAddress() != null && !search.getIpAddress().isBlank()) {
            where.and(log.ipAddress.containsIgnoreCase(search.getIpAddress()));
        }

        if (search.getAtFrom() != null && search.getAtTo() == null) {
            where.and(log.at.goe(search.getAtFrom()));
        } else if (search.getAtFrom() != null) {
            where.and(log.at.between(search.getAtFrom(), search.getAtTo()));
        }

        List<EmployeeChangeLog> rows = queryFactory
                .selectFrom(log)
                .where(where)
                .orderBy(getOrderSpecifier(search.getSortField(), search.getSortDirection()))
                .limit(size + 1L)
                .fetch();

        boolean hasNext = rows.size() > size;
        if (hasNext) rows = rows.subList(0, size);

        return new SliceImpl<>(rows, PageRequest.ofSize(size), hasNext);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortField, String sortDirection) {
        boolean isAsc = "asc".equalsIgnoreCase(sortDirection);

        return switch (sortField) {
            case "ipAddress" -> isAsc ? log.ipAddress.asc() : log.ipAddress.desc();
            case "at" -> isAsc ? log.at.asc() : log.at.desc();
            default -> log.at.desc();
        };
    }
}
