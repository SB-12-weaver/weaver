package com.sbproject.weaver.changelog.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiffResponse {
    private String propertyName;
    private String before;
    private String after;
}
