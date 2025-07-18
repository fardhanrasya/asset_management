package com.fardhan.assetmanagement.service;

import com.fardhan.assetmanagement.dto.request.CreateAssetTypeRequest;
import com.fardhan.assetmanagement.entity.AssetCategory;
import com.fardhan.assetmanagement.entity.AssetType;
import com.fardhan.assetmanagement.repository.AssetCategoryRepository;
import com.fardhan.assetmanagement.repository.AssetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetTypeService {

    private final AssetTypeRepository assetTypeRepository;
    private final AssetCategoryRepository assetCategoryRepository;

    public AssetType create(CreateAssetTypeRequest request) {
        AssetCategory category = assetCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        AssetType assetType = new AssetType();
        assetType.setName(request.getName());
        assetType.setCategory(category);

        return assetTypeRepository.save(assetType);
    }

    public java.util.List<AssetType> findAll() {
        return assetTypeRepository.findAll();
    }

    public java.util.Optional<AssetType> findById(java.util.UUID id) {
        return assetTypeRepository.findById(id);
    }
}
