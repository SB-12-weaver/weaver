package com.sbproject.weaver.changelog.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiffDto {
    private String propertyName;
    private String before;
    private String after;
}
