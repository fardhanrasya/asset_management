package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.Asset;
import com.fardhan.assetmanagement.entity.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByAssetCategory(AssetCategory assetCategory);
}