package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreateAssetCategoryRequest;
import com.fardhan.assetmanagement.entity.AssetCategory;
import com.fardhan.assetmanagement.repository.AssetCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetCategoryService {

    private final AssetCategoryRepository assetCategoryRepository;

    public AssetCategory create(CreateAssetCategoryRequest request) {
        AssetCategory assetCategory = new AssetCategory();
        assetCategory.setName(request.getName());

        if (request.getParentId() != null) {
            AssetCategory parent = assetCategoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            assetCategory.setParent(parent);
        }

        return assetCategoryRepository.save(assetCategory);
    }

    public java.util.List<AssetCategory> findAll() {
        return assetCategoryRepository.findAll();
    }

    public java.util.Optional<AssetCategory> findById(java.util.UUID id) {
        return assetCategoryRepository.findById(id);
    }
}
