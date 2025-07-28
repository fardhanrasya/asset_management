package com.fardhan.assetmanagement.dto.request;

import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;

@Data
public class CreateRepairRequestRequest {
    private UUID assetId;
    private String reason;
    private String treatment;
    private BigDecimal estimateCost;
    private Integer estimateDurationDays;
    private String type; // "REPAIR" or "SELF_REPAIR"
}