package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreatePurchaseRequestRequest;
import com.fardhan.assetmanagement.dto.response.PurchaseRequestResponse;
import com.fardhan.assetmanagement.entity.*;
import com.fardhan.assetmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {
    private final RequestRepository requestRepository;
    private final PurchaseRequestDetailRepository purchaseRequestDetailRepository;
    private final UserRepository userRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final LogActivityService logActivityService;

    private static final BigDecimal APPROVAL_LIMIT = new BigDecimal("10000000"); // batas harga hardcoded

    @Transactional
    public PurchaseRequestResponse createPurchaseRequest(CreatePurchaseRequestRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getRole() != User.UserRole.HRGA) {
            throw new SecurityException("Only HRGA can create purchase requests");
        }
        RequestType type = requestTypeRepository.findByNameIgnoreCase("PURCHASE")
                .orElseThrow(() -> new IllegalArgumentException("Request type 'PURCHASE' not found"));
        Request req = new Request();
        req.setRequestor(user);
        req.setType(type);
        req.setStatus(Request.RequestStatus.OPEN);
        req.setReason(request.getReason());
        req = requestRepository.save(req);
        PurchaseRequestDetail detail = new PurchaseRequestDetail();
        detail.setRequest(req);
        detail.setAssetDisplayName(request.getAssetDisplayName());
        detail.setQuantity(request.getQuantity());
        detail.setEstimateCost(request.getEstimateCost());
        if (request.getEstimateCost().compareTo(APPROVAL_LIMIT) > 0) {
            detail.setStatus(PurchaseRequestDetail.PurchaseStatus.PENDING);
        } else {
            detail.setStatus(PurchaseRequestDetail.PurchaseStatus.ONPURCHASE);
        }
        purchaseRequestDetailRepository.save(detail);
        PurchaseRequestResponse response = toResponse(req, detail, user);
        logActivityService
                .log("Purchase request for '" + detail.getAssetDisplayName() + "' created by " + user.getName(), user);
        return response;
    }

    public List<PurchaseRequestResponse> getAll() {
        return purchaseRequestDetailRepository.findAll().stream()
                .map(detail -> toResponse(detail.getRequest(), detail, detail.getRequest().getRequestor()))
                .collect(Collectors.toList());
    }

    public List<PurchaseRequestResponse> getMine(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return purchaseRequestDetailRepository.findAll().stream()
                .filter(detail -> detail.getRequest().getRequestor().getId().equals(user.getId()))
                .map(detail -> toResponse(detail.getRequest(), detail, user))
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseRequestResponse approve(UUID requestId, String directorEmail, boolean accepted,
            String reviewComment) {
        User director = userRepository.findByEmail(directorEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (director.getRole() != User.UserRole.DIREKTUR) {
            throw new SecurityException("Only DIREKTUR can approve purchase requests");
        }
        Request req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        PurchaseRequestDetail detail = purchaseRequestDetailRepository.findAll().stream()
                .filter(d -> d.getRequest().getId().equals(req.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Purchase request detail not found"));
        if (detail.getStatus() != PurchaseRequestDetail.PurchaseStatus.PENDING) {
            throw new IllegalStateException("Request is not pending approval");
        }
        req.setReviewComment(reviewComment);
        if (accepted) {
            detail.setStatus(PurchaseRequestDetail.PurchaseStatus.ONPURCHASE);
            req.setStatus(Request.RequestStatus.ACCEPTED);
        } else {
            detail.setStatus(PurchaseRequestDetail.PurchaseStatus.REJECTED);
            req.setStatus(Request.RequestStatus.REJECTED);
        }
        requestRepository.save(req);
        purchaseRequestDetailRepository.save(detail);
        String action = accepted ? "approved" : "rejected";
        logActivityService.log(
                "Purchase request '" + detail.getAssetDisplayName() + "' " + action + " by " + director.getName(),
                director);
        return toResponse(req, detail, req.getRequestor());
    }

    @Transactional
    public PurchaseRequestResponse close(UUID requestId, String hrgaEmail, String provider, BigDecimal actualCost) {
        User hrga = userRepository.findByEmail(hrgaEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (hrga.getRole() != User.UserRole.HRGA) {
            throw new SecurityException("Only HRGA can close purchase requests");
        }
        Request req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        PurchaseRequestDetail detail = purchaseRequestDetailRepository.findAll().stream()
                .filter(d -> d.getRequest().getId().equals(req.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Purchase request detail not found"));
        detail.setStatus(PurchaseRequestDetail.PurchaseStatus.CLOSED);
        detail.setProvider(provider);
        detail.setActualCost(actualCost);
        purchaseRequestDetailRepository.save(detail);
        logActivityService.log("Purchase request '" + detail.getAssetDisplayName() + "' closed by " + hrga.getName(),
                hrga);
        return toResponse(req, detail, req.getRequestor());
    }

    private PurchaseRequestResponse toResponse(Request req, PurchaseRequestDetail detail, User user) {
        return PurchaseRequestResponse.builder()
                .requestId(req.getId())
                .assetDisplayName(detail.getAssetDisplayName())
                .quantity(detail.getQuantity())
                .status(detail.getStatus().name())
                .estimateCost(detail.getEstimateCost())
                .actualCost(detail.getActualCost())
                .provider(detail.getProvider())
                .reason(req.getReason())
                .reviewComment(req.getReviewComment())
                .requestedAt(req.getRequestedAt())
                .requestorName(user.getName())
                .requestorEmail(user.getEmail())
                .build();
    }
}