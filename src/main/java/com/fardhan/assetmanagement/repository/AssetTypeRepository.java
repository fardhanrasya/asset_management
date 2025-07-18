package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, UUID> {
}
