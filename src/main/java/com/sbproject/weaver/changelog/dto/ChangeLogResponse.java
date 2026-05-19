package com.sbproject.weaver.changelog.dto;

import com.sbproject.weaver.changelog.entity.ChangeLogType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLogResponse {
    private UUID id;
    private ChangeLogType type;
    private String employeeNumber;
    private String memo;
    private String ipAddress;
    private Instant at;

}
