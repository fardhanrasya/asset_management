package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.response.LogActivityResponse;
import com.fardhan.assetmanagement.service.LogActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogActivityController {
    private final LogActivityService logActivityService;

    @PreAuthorize("hasRole('HRGA')")
    @GetMapping
    public ResponseEntity<List<LogActivityResponse>> getAllLogs() {
        return ResponseEntity.ok(logActivityService.getAllLogs());
    }
}