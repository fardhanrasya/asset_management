package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.RequestAssignDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestAssignDetailRepository extends JpaRepository<RequestAssignDetail, UUID> {
    List<RequestAssignDetail> findByTargetUser_Id(UUID targetUserId);

    Optional<RequestAssignDetail> findByRequest_Id(UUID requestId);
}