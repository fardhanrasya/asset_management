package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.RequestAssetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestAssetDetailRepository extends JpaRepository<RequestAssetDetail, UUID> {
}