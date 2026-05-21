package com.sbproject.weaver.file.controller;

import com.sbproject.weaver.file.dto.FileResponse;
import com.sbproject.weaver.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID fileId) {
        FileResponse file = fileService.findById(fileId);
        Resource resource = fileService.downloadResource(fileId);

        String encodedFileName = URLEncoder.encode(file.getOriginalName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .contentLength(file.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }
}