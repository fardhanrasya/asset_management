package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
}