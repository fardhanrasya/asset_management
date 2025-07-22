package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.PurchaseRequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseRequestDetailRepository extends JpaRepository<PurchaseRequestDetail, UUID> {
}