package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreateRepairRequestRequest;
import com.fardhan.assetmanagement.dto.request.UpdateRepairStatusRequest;
import com.fardhan.assetmanagement.dto.response.RepairRequestResponse;
import com.fardhan.assetmanagement.service.RepairRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/repair-requests")
@RequiredArgsConstructor
public class RepairRequestController {
    private final RepairRequestService repairRequestService;

    @PostMapping
    @PreAuthorize("hasRole('KARYAWAN')")
    public ResponseEntity<RepairRequestResponse> create(@RequestBody CreateRepairRequestRequest request,
            Principal principal) {
        String email = principal.getName();
        RepairRequestResponse response = repairRequestService.createRepairRequest(request, email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<List<RepairRequestResponse>> getAll() {
        List<RepairRequestResponse> responses = repairRequestService.getAllRepairRequests();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('KARYAWAN')")
    public ResponseEntity<List<RepairRequestResponse>> getMyRequests(Principal principal) {
        String email = principal.getName();
        List<RepairRequestResponse> responses = repairRequestService.getMyRepairRequests(email);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/status")
    @PreAuthorize("hasAnyRole('HRGA', 'KARYAWAN')")
    public ResponseEntity<RepairRequestResponse> updateStatus(@RequestBody UpdateRepairStatusRequest request,
            Principal principal) {
        String email = principal.getName();
        RepairRequestResponse response = repairRequestService.updateRepairStatus(request, email);
        return ResponseEntity.ok(response);
    }
}