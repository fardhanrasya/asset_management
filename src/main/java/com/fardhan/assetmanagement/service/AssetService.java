package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreateAssetRequest;
import com.fardhan.assetmanagement.dto.request.UpdateAssetRequest;
import com.fardhan.assetmanagement.dto.response.AssetResponse;
import com.fardhan.assetmanagement.entity.*;
import com.fardhan.assetmanagement.repository.AssetCategoryRepository;
import com.fardhan.assetmanagement.repository.AssetRepository;
import com.fardhan.assetmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final UserRepository userRepository;

    public AssetService(AssetRepository assetRepository, AssetCategoryRepository assetCategoryRepository,
            UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.assetCategoryRepository = assetCategoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AssetResponse createAsset(CreateAssetRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRole() != User.UserRole.HRGA) {
            throw new AccessDeniedException("Only HRGA can add asset");
        }
        AssetCategory category = assetCategoryRepository.findById(request.getAssetCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Asset category not found"));
        Asset asset = new Asset();
        asset.setAssetCategory(category);
        asset.setAssetStatus(Asset.AssetStatus.AVAILABLE);
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setModelName(request.getModelName());
        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AssetResponse getAssetById(UUID id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
        return toResponse(asset);
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> getAssetsByCategory(UUID categoryId) {
        AssetCategory category = assetCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Asset category not found"));
        return assetRepository.findByAssetCategory(category).stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAsset(UUID assetId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRole() != User.UserRole.HRGA) {
            throw new AccessDeniedException("Only HRGA can delete asset");
        }
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
        assetRepository.delete(asset);
    }

    @Transactional
    public AssetResponse updateAsset(UUID assetId, UpdateAssetRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRole() != User.UserRole.HRGA) {
            throw new AccessDeniedException("Only HRGA can update asset");
        }
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
        if (request.getModelName() != null) {
            asset.setModelName(request.getModelName());
        }
        if (request.getPurchasePrice() != null) {
            asset.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getAssetCategoryId() != null) {
            AssetCategory category = assetCategoryRepository.findById(request.getAssetCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Asset category not found"));
            asset.setAssetCategory(category);
        }
        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    private AssetResponse toResponse(Asset asset) {
        return AssetResponse.builder()
                .id(asset.getId())
                .modelName(asset.getModelName())
                .assetStatus(asset.getAssetStatus().name())
                .purchasePrice(asset.getPurchasePrice())
                .totalMaintenancePrice(asset.getTotalMaintenancePrice())
                .assetCategoryId(asset.getAssetCategory() != null ? asset.getAssetCategory().getId() : null)
                .assetCategoryName(asset.getAssetCategory() != null ? asset.getAssetCategory().getName() : null)
                .currentHolderId(asset.getCurrentHolder() != null ? asset.getCurrentHolder().getId() : null)
                .currentHolderName(asset.getCurrentHolder() != null ? asset.getCurrentHolder().getName() : null)
                .createdAt(asset.getCreatedAt())
                .build();
    }
}