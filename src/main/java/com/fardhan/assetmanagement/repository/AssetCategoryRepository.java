package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, UUID> {
}
