package com.sbproject.weaver.changelog.service;

import com.sbproject.weaver.changelog.dto.ChangeLogDto;
import com.sbproject.weaver.changelog.dto.ChangeLogResponse;
import com.sbproject.weaver.changelog.dto.ChangeLogSearchRequest;
import com.sbproject.weaver.common.dto.CursorPageResponse;


public interface ChangeLogService {

    CursorPageResponse<ChangeLogDto> search(String cursor, int size, ChangeLogSearchRequest searchRequest);

}
