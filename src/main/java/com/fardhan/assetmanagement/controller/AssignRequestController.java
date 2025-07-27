package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreateAssignRequestRequest;
import com.fardhan.assetmanagement.dto.response.AssignRequestResponse;
import com.fardhan.assetmanagement.entity.User;
import com.fardhan.assetmanagement.repository.UserRepository;
import com.fardhan.assetmanagement.service.AssignRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assign-requests")
@RequiredArgsConstructor
public class AssignRequestController {
    private final AssignRequestService assignRequestService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('HRGA')")
    public ResponseEntity<AssignRequestResponse> createAssignRequest(
            @Valid @RequestBody CreateAssignRequestRequest request,
            Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(assignRequestService.createAssignRequest(request, user));
    }

    @GetMapping
    public ResponseEntity<List<AssignRequestResponse>> getAssignRequests(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(assignRequestService.getAssignRequests(user));
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<AssignRequestResponse> acceptAssignRequest(
            @PathVariable UUID requestId,
            @RequestParam(required = false) String reviewComment,
            Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(assignRequestService.respondToAssignRequest(requestId, true, reviewComment, user));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<AssignRequestResponse> rejectAssignRequest(
            @PathVariable UUID requestId,
            @RequestParam(required = false) String reviewComment,
            Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(assignRequestService.respondToAssignRequest(requestId, false, reviewComment, user));
    }
}