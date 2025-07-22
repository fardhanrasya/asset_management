package com.fardhan.assetmanagement.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateAssetRequest {
    private UUID assetCategoryId;
    private BigDecimal purchasePrice;
    private String modelName;
}