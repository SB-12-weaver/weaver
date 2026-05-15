package com.sbproject.weaver.common.exception;


import com.sbproject.weaver.common.dto.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - 데이터 없음
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResult<Void>> handleNotFound(NoSuchElementException e) {
        ApiResult<Void> body = ApiResult.fail("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // 400 - 잘못된 요청 (파라미터 오류, 파트 누락, 타입 불일치)
    @ExceptionHandler({
            IllegalArgumentException.class,
            MissingServletRequestPartException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiResult<Void>> handleBadRequest(Exception e) {
        ApiResult<Void> body = ApiResult.fail("BAD_REQUEST", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 409 - 중복 데이터 (username, email unique 제약 위반)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResult<Void>> handleDataIntegrity(DataIntegrityViolationException e) {
        ApiResult<Void> body = ApiResult.fail("CONFLICT", "이미 존재하는 데이터입니다.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 413 - 파일 크기 초과
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResult<Void>> handleMaxUpload(MaxUploadSizeExceededException e) {
        ApiResult<Void> body = ApiResult.fail("PAYLOAD_TOO_LARGE", "업로드 가능한 파일 크기를 초과했습니다.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(body);
    }

    // 500 - 파일 I/O 오류 (FileStorage 저장/삭제 실패)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResult<Void>> handleIO(IOException e) {
        ApiResult<Void> body = ApiResult.fail("IO_ERROR", "파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 500 - RuntimeException (FileStorage RuntimeException 포함)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResult<Void>> handleRuntime(RuntimeException e, HttpServletRequest req) {
        e.printStackTrace();
        ApiResult<Void> body = ApiResult.fail("RUNTIME_ERROR", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 500 - 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleEtc(Exception e, HttpServletRequest req) {
        e.printStackTrace();
        ApiResult<Void> body = ApiResult.fail("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}