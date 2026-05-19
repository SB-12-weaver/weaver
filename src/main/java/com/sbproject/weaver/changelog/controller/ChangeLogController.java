package com.sbproject.weaver.changelog.controller;

import com.sbproject.weaver.changelog.dto.ChangeLogResponse;
import com.sbproject.weaver.changelog.dto.ChangeLogSearchRequest;
import com.sbproject.weaver.changelog.service.ChangeLogService;
import com.sbproject.weaver.common.dto.CursorPageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class ChangeLogController {

    private final ChangeLogService changeLogService;

    @GetMapping("")
    public ResponseEntity<CursorPageResponse<ChangeLogResponse>> search(
            HttpServletRequest httpRequest, //추후 삭제
            @RequestParam(required = false) Long nextIdAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam int size,
            @ModelAttribute ChangeLogSearchRequest searchRequest
    ) {

        httpRequest.getParameterMap().forEach((key, values) ->
                System.out.println("key: " + key + " / value: " + values[0])
        );
        // 데이터 수신 확인용 콘솔 출력
        System.out.println("====== 프론트엔드 요청 데이터 ======");
        System.out.println("idAfter: " + nextIdAfter);
        System.out.println("cursor: " + cursor);
        System.out.println("size : " + size);
        System.out.println("searchRequest: " + searchRequest);
        System.out.println(searchRequest.getSortField());
        System.out.println(searchRequest.getSortDirection());
        System.out.println("=================================");

        return ResponseEntity.ok(changeLogService.search(cursor, size, searchRequest));



    }



}
