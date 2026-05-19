package com.sbproject.weaver.changelog.dto;

import com.sbproject.weaver.changelog.entity.ChangeLogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class ChangeLogSearchRequest {
    private String employeeNumber;
    private String type;
    private String memo;
    private String ipAddress;
    private Instant atFrom;
    private Instant atTo;
    private String sortField;
    private String sortDirection;

    @ConstructorProperties({
            "employeeNumber", "type", "memo", "ipAddress",
            "atFrom", "atTo", "sortField", "sortDirection"
    })
    public ChangeLogSearchRequest(String employeeNumber, String type,
                                  String memo, String ipAddress,
                                  Instant atFrom, Instant atTo,
                                  String sortField, String sortDirection) {
        this.employeeNumber = employeeNumber;
        this.type = type;
        this.memo = memo;
        this.ipAddress = ipAddress;
        this.atFrom = atFrom;
        this.atTo = atTo;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }

    public ChangeLogSearchRequest withDefaults() {
        return ChangeLogSearchRequest.builder()
                .employeeNumber(this.employeeNumber)
                .type(this.type)
                .memo(this.memo)
                .ipAddress(this.ipAddress)
                .atFrom(this.atFrom)
                .atTo(this.atTo)
                .sortField(this.sortField != null ? this.sortField : "at")
                .sortDirection(this.sortDirection != null ? this.sortDirection : "desc")
                .build();
    }
}
