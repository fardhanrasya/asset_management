package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreateAssignRequestRequest;
import com.fardhan.assetmanagement.dto.response.AssignRequestResponse;
import com.fardhan.assetmanagement.entity.*;
import com.fardhan.assetmanagement.exception.BadRequestException;
import com.fardhan.assetmanagement.exception.ForbiddenException;
import com.fardhan.assetmanagement.exception.NotFoundException;
import com.fardhan.assetmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignRequestService {
    private final RequestRepository requestRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final RequestAssignDetailRepository requestAssignDetailRepository;
    private final LogActivityService logActivityService;

    @Transactional
    public AssignRequestResponse createAssignRequest(CreateAssignRequestRequest createRequest, User currentUser) {
        // Validate that current user has HRGA role
        if (currentUser.getRole() != User.UserRole.HRGA) {
            throw new ForbiddenException("Only HRGA can create assign requests");
        }

        // Get and validate target user exists
        User targetUser = userRepository.findById(createRequest.getTargetUserId())
                .orElseThrow(() -> new NotFoundException("Target user not found"));

        // Get and validate asset exists and is available
        Asset asset = assetRepository.findById(createRequest.getAssetId())
                .orElseThrow(() -> new NotFoundException("Asset not found"));

        if (asset.getCurrentHolder() != null) {
            throw new BadRequestException("Asset is already assigned to a user");
        }

        // Get request type for assign requests
        RequestType assignRequestType = requestTypeRepository.findByNameIgnoreCase("ASSIGN")
                .orElseThrow(() -> new NotFoundException("Assign request type not found"));

        // Create request
        Request request = new Request();
        request.setRequestor(currentUser);
        request.setType(assignRequestType);
        request.setStatus(Request.RequestStatus.OPEN);
        request.setReason(createRequest.getReason());
        request = requestRepository.save(request);

        // Create request assign detail
        RequestAssignDetail detail = new RequestAssignDetail();
        detail.setRequest(request);
        detail.setAsset(asset);
        detail.setTargetUser(targetUser);
        requestAssignDetailRepository.save(detail);

        AssignRequestResponse response = buildAssignRequestResponse(request, detail);
        logActivityService.log("Assign request for asset '" + asset.getModelName() + "' to user '"
                + targetUser.getName() + "' created by " + currentUser.getName(), currentUser);
        return response;
    }

    @Transactional(readOnly = true)
    public List<AssignRequestResponse> getAssignRequests(User currentUser) {
        return requestAssignDetailRepository.findByTargetUser_Id(currentUser.getId())
                .stream()
                .map(detail -> buildAssignRequestResponse(detail.getRequest(), detail))
                .toList();
    }

    @Transactional
    public AssignRequestResponse respondToAssignRequest(UUID requestId, boolean accept, String reviewComment,
            User currentUser) {
        RequestAssignDetail detail = requestAssignDetailRepository.findByRequest_Id(requestId)
                .orElseThrow(() -> new NotFoundException("Assign request not found"));

        Request request = detail.getRequest();

        // Validate that current user is the target user
        if (!detail.getTargetUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can only respond to your own assign requests");
        }

        // Validate request is still open
        if (request.getStatus() != Request.RequestStatus.OPEN) {
            throw new BadRequestException("Request is no longer open");
        }

        // Update request status
        request.setStatus(accept ? Request.RequestStatus.ACCEPTED : Request.RequestStatus.REJECTED);
        request.setReviewComment(reviewComment);
        request = requestRepository.save(request);

        // If accepted, update asset holder and status
        if (accept) {
            Asset asset = detail.getAsset();
            asset.setCurrentHolder(detail.getTargetUser());
            asset.setAssetStatus(Asset.AssetStatus.ASSIGNED);
            assetRepository.save(asset);
        }

        AssignRequestResponse response = buildAssignRequestResponse(request, detail);
        String action = accept ? "accepted" : "rejected";
        logActivityService.log("Assign request for asset '" + detail.getAsset().getModelName() + "' " + action + " by "
                + currentUser.getName(), currentUser);
        return response;
    }

    private AssignRequestResponse buildAssignRequestResponse(Request request, RequestAssignDetail detail) {
        return AssignRequestResponse.builder()
                .id(request.getId())
                .assetId(detail.getAsset().getId())
                .assetName(detail.getAsset().getModelName())
                .targetUserName(detail.getTargetUser().getName())
                .targetUserEmail(detail.getTargetUser().getEmail())
                .status(request.getStatus().name())
                .reason(request.getReason())
                .reviewComment(request.getReviewComment())
                .requestedAt(request.getRequestedAt())
                .requestorName(request.getRequestor().getName())
                .requestorEmail(request.getRequestor().getEmail())
                .build();
    }
}