package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreatePurchaseRequestRequest;
import com.fardhan.assetmanagement.dto.response.PurchaseRequestResponse;
import com.fardhan.assetmanagement.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-requests")
@RequiredArgsConstructor
public class PurchaseRequestController {
    private final PurchaseRequestService purchaseRequestService;

    @PostMapping
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<PurchaseRequestResponse> create(@RequestBody CreatePurchaseRequestRequest request,
            Principal principal) {
        String email = principal.getName();
        PurchaseRequestResponse response = purchaseRequestService.createPurchaseRequest(request, email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<List<PurchaseRequestResponse>> getAll() {
        List<PurchaseRequestResponse> responses = purchaseRequestService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<List<PurchaseRequestResponse>> getMine(Principal principal) {
        String email = principal.getName();
        List<PurchaseRequestResponse> responses = purchaseRequestService.getMine(email);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{requestId}/approve")
    @PreAuthorize("hasRole('DIREKTUR')")
    public ResponseEntity<PurchaseRequestResponse> approve(@PathVariable UUID requestId, @RequestParam boolean accepted,
            @RequestParam(required = false) String reviewComment, Principal principal) {
        String email = principal.getName();
        PurchaseRequestResponse response = purchaseRequestService.approve(requestId, email, accepted, reviewComment);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{requestId}/close")
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<PurchaseRequestResponse> close(@PathVariable UUID requestId, @RequestParam String provider,
            @RequestParam BigDecimal actualCost, Principal principal) {
        String email = principal.getName();
        PurchaseRequestResponse response = purchaseRequestService.close(requestId, email, provider, actualCost);
        return ResponseEntity.ok(response);
    }
}