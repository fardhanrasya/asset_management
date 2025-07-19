package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, UUID> {
    Optional<RequestType> findByNameIgnoreCase(String name);
}