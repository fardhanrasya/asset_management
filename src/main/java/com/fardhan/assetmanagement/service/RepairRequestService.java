package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreateRepairRequestRequest;
import com.fardhan.assetmanagement.dto.request.UpdateRepairStatusRequest;
import com.fardhan.assetmanagement.dto.response.RepairRequestResponse;
import com.fardhan.assetmanagement.entity.*;
import com.fardhan.assetmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairRequestService {
    private final RequestRepository requestRepository;
    private final RepairRequestDetailRepository repairRequestDetailRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final RequestTypeRepository requestTypeRepository;

    @Transactional
    public RepairRequestResponse createRepairRequest(CreateRepairRequestRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
        RequestType type = requestTypeRepository.findByNameIgnoreCase(request.getType())
                .orElseThrow(() -> new IllegalArgumentException("Request type not found"));
        Request req = new Request();
        req.setRequestor(user);
        req.setType(type);
        req.setStatus(Request.RequestStatus.OPEN);
        req.setReason(request.getReason());
        req = requestRepository.save(req);
        RepairRequestDetail detail = new RepairRequestDetail();
        detail.setRequest(req);
        detail.setAsset(asset);
        detail.setTreatment(request.getTreatment());
        detail.setEstimateCost(request.getEstimateCost());
        detail.setEstimateDurationDays(request.getEstimateDurationDays());
        detail.setStatus(RepairRequestDetail.RepairStatus.PENDING);
        repairRequestDetailRepository.save(detail);
        return toResponse(detail, req, user, asset, type);
    }

    public List<RepairRequestResponse> getAllRepairRequests() {
        List<RepairRequestDetail> details = repairRequestDetailRepository.findAll();
        return details.stream().map(detail -> {
            Request req = detail.getRequest();
            User user = req.getRequestor();
            Asset asset = detail.getAsset();
            RequestType type = req.getType();
            return toResponse(detail, req, user, asset, type);
        }).collect(Collectors.toList());
    }

    public List<RepairRequestResponse> getMyRepairRequests(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<RepairRequestDetail> details = repairRequestDetailRepository.findAll().stream()
                .filter(detail -> detail.getRequest().getRequestor().getId().equals(user.getId()))
                .collect(Collectors.toList());
        return details.stream().map(detail -> {
            Request req = detail.getRequest();
            Asset asset = detail.getAsset();
            RequestType type = req.getType();
            return toResponse(detail, req, user, asset, type);
        }).collect(Collectors.toList());
    }

    @Transactional
    public RepairRequestResponse updateRepairStatus(UpdateRepairStatusRequest request, String userEmail) {
        RepairRequestDetail detail = repairRequestDetailRepository.findById(request.getRepairRequestDetailId())
                .orElseThrow(() -> new IllegalArgumentException("Repair request detail not found"));
        Request req = detail.getRequest();
        RequestType type = req.getType();
        Asset asset = detail.getAsset();

        // Status transitions
        if (request.getStatus() != null) {
            if (request.getStatus().equalsIgnoreCase("ONSERVICE")) {
                if (request.getProvider() == null || request.getStartDate() == null) {
                    throw new IllegalArgumentException("Provider and start date are required for ONSERVICE status");
                }
                detail.setProvider(request.getProvider());
                detail.setStartDate(request.getStartDate());
                detail.setStatus(RepairRequestDetail.RepairStatus.ONSERVICE);

                // Set asset status to UNAVAILABLE
                if (asset != null) {
                    asset.setAssetStatus(Asset.AssetStatus.UNAVAILABLE);
                    assetRepository.save(asset);
                }
            } else if (request.getStatus().equalsIgnoreCase("CLOSED")) {
                if (request.getActualCost() == null || request.getTreatment() == null || request.getEndDate() == null) {
                    throw new IllegalArgumentException(
                            "Actual cost, treatment, and end date are required for CLOSED status");
                }
                detail.setActualCost(request.getActualCost());
                detail.setTreatment(request.getTreatment());
                detail.setEndDate(request.getEndDate());
                // Optionally handle warranty if needed
                detail.setStatus(RepairRequestDetail.RepairStatus.CLOSED);

                // Set asset status to AVAILABLE and update total maintenance price
                if (asset != null) {
                    asset.setAssetStatus(Asset.AssetStatus.AVAILABLE);

                    // Update total maintenance price with actual cost
                    if (request.getActualCost() != null) {
                        if (asset.getTotalMaintenancePrice() == null) {
                            asset.setTotalMaintenancePrice(request.getActualCost());
                        } else {
                            asset.setTotalMaintenancePrice(asset.getTotalMaintenancePrice().add(request.getActualCost()));
                        }
                    }

                    assetRepository.save(asset);
                }
            } else if (request.getStatus().equalsIgnoreCase("CANCELLED")) {
                detail.setStatus(RepairRequestDetail.RepairStatus.CANCELLED);
            }
        }
        repairRequestDetailRepository.save(detail);
        return toResponse(detail, req, req.getRequestor(), detail.getAsset(), type);
    }

    private RepairRequestResponse toResponse(RepairRequestDetail detail, Request req, User user, Asset asset,
            RequestType type) {
        return RepairRequestResponse.builder()
                .id(detail.getId())
                .requestId(req.getId())
                .assetId(asset.getId())
                .assetName(asset.getModelName())
                .provider(detail.getProvider())
                .treatment(detail.getTreatment())
                .estimateCost(detail.getEstimateCost())
                .estimateDurationDays(detail.getEstimateDurationDays())
                .status(detail.getStatus().name())
                .startDate(detail.getStartDate())
                .endDate(detail.getEndDate())
                .actualCost(detail.getActualCost())
                .reason(req.getReason())
                .requestorName(user.getName())
                .requestorEmail(user.getEmail())
                .type(type.getName())
                .build();
    }
}