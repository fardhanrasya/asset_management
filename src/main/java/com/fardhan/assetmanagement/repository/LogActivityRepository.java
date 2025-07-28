package com.fardhan.assetmanagement.repository;

import com.fardhan.assetmanagement.entity.LogActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LogActivityRepository extends JpaRepository<LogActivity, UUID> {
}