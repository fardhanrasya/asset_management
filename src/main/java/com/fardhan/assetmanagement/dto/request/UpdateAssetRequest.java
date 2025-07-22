package com.fardhan.assetmanagement.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateAssetRequest {
    private String modelName;
    private BigDecimal purchasePrice;
    private UUID assetCategoryId;
}