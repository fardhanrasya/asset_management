package com.fardhan.assetmanagement.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreatePurchaseRequestRequest {
    private String assetDisplayName;
    private Integer quantity;
    private BigDecimal estimateCost;
    private String reason;
}