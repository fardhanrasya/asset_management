package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AssetResponse {
    private UUID id;
    private String modelName;
    private String assetStatus;
    private BigDecimal purchasePrice;
    private BigDecimal totalMaintenancePrice;
    private UUID assetCategoryId;
    private String assetCategoryName;
    private UUID currentHolderId;
    private String currentHolderName;
    private LocalDateTime createdAt;
}