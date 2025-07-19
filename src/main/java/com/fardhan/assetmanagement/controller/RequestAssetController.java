package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreateRequestAssetRequest;
import com.fardhan.assetmanagement.dto.request.UpdateRequestStatusRequest;
import com.fardhan.assetmanagement.dto.response.RequestAssetResponse;
import com.fardhan.assetmanagement.service.RequestAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/request-assets")
@RequiredArgsConstructor
public class RequestAssetController {
    private final RequestAssetService requestAssetService;

    @PostMapping
    @PreAuthorize("hasRole('KARYAWAN')")
    public ResponseEntity<RequestAssetResponse> create(@RequestBody CreateRequestAssetRequest request,
            Principal principal) {
        String email = principal.getName();
        RequestAssetResponse response = requestAssetService.createRequestAsset(request, email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<List<RequestAssetResponse>> getAll() {
        List<RequestAssetResponse> responses = requestAssetService.getAllRequestAssets();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('KARYAWAN')")
    public ResponseEntity<List<RequestAssetResponse>> getMyRequests(Principal principal) {
        String email = principal.getName();
        List<RequestAssetResponse> responses = requestAssetService.getMyRequestAssets(email);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<RequestAssetResponse> updateStatus(@RequestBody UpdateRequestStatusRequest request,
            Principal principal) {
        String email = principal.getName();
        RequestAssetResponse response = requestAssetService.updateRequestStatus(request, email);
        return ResponseEntity.ok(response);
    }
}