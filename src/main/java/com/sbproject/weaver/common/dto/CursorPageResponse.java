package com.sbproject.weaver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CursorPageResponse<T> {
    int size;
    boolean hasNext;
    Object nextCursor;
    List<T> items;
    int totalElements;
    int nextIdAfter;
}
