package com.sbproject.weaver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CursorPageResponse<T> {
    private List<T> content;
    private String nextCursor;
    private Long nextIdAfter;
    private int size;
    private long totalElements;
    private boolean hasNext;
}
