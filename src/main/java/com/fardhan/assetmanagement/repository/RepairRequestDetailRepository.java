package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.RepairRequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RepairRequestDetailRepository extends JpaRepository<RepairRequestDetail, UUID> {
}