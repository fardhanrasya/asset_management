package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreateRequestAssetRequest;
import com.fardhan.assetmanagement.dto.request.UpdateRequestStatusRequest;
import com.fardhan.assetmanagement.dto.response.RequestAssetResponse;
import com.fardhan.assetmanagement.entity.*;
import com.fardhan.assetmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestAssetService {
    private final RequestRepository requestRepository;
    private final RequestAssetDetailRepository requestAssetDetailRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final UserRepository userRepository;
    private final RequestTypeRepository requestTypeRepository;

    @Transactional
    public RequestAssetResponse createRequestAsset(CreateRequestAssetRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        AssetCategory category = assetCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        RequestType type = requestTypeRepository.findByNameIgnoreCase("ASSET")
                .orElseThrow(() -> new IllegalArgumentException("Request type 'ASSET' not found"));
        Request req = new Request();
        req.setRequestor(user);
        req.setType(type);
        req.setStatus(Request.RequestStatus.OPEN);
        req.setReason(request.getReason());
        req = requestRepository.save(req);
        RequestAssetDetail detail = new RequestAssetDetail();
        detail.setRequest(req);
        detail.setCategory(category);
        detail.setQuantity(request.getQuantity());
        requestAssetDetailRepository.save(detail);
        return toResponse(req, detail, user, category);
    }

    public List<RequestAssetResponse> getAllRequestAssets() {
        List<RequestAssetDetail> details = requestAssetDetailRepository.findAll();
        return details.stream().map(detail -> {
            Request request = detail.getRequest();
            User user = request.getRequestor();
            AssetCategory category = detail.getCategory();
            return toResponse(request, detail, user, category);
        }).collect(Collectors.toList());
    }

    public List<RequestAssetResponse> getMyRequestAssets(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<RequestAssetDetail> details = requestAssetDetailRepository.findAll().stream()
                .filter(detail -> detail.getRequest().getRequestor().getId().equals(user.getId()))
                .toList();
        return details.stream().map(detail -> {
            Request request = detail.getRequest();
            AssetCategory category = detail.getCategory();
            return toResponse(request, detail, user, category);
        }).toList();
    }

    @Transactional
    public RequestAssetResponse updateRequestStatus(UpdateRequestStatusRequest request, String reviewerEmail) {
        Request req = requestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (!req.getType().getName().equalsIgnoreCase("ASSET")) {
            throw new IllegalArgumentException("Not an asset request");
        }
        req.setStatus(Request.RequestStatus.valueOf(request.getStatus()));
        req.setReviewComment(request.getReviewComment());
        final Request savedReq = requestRepository.save(req);
        final RequestAssetDetail detail = requestAssetDetailRepository.findAll().stream()
                .filter(d -> d.getRequest().getId().equals(savedReq.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Request asset detail not found"));
        final User user = savedReq.getRequestor();
        final AssetCategory category = detail.getCategory();
        return toResponse(savedReq, detail, user, category);
    }

    private RequestAssetResponse toResponse(Request req, RequestAssetDetail detail, User user, AssetCategory category) {
        return RequestAssetResponse.builder()
                .id(req.getId())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .quantity(detail.getQuantity())
                .status(req.getStatus().name())
                .reason(req.getReason())
                .reviewComment(req.getReviewComment())
                .requestedAt(req.getRequestedAt())
                .requestorName(user.getName())
                .requestorEmail(user.getEmail())
                .build();
    }
}