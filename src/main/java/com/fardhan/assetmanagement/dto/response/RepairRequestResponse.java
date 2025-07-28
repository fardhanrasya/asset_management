package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RepairRequestResponse {
    private UUID id;
    private UUID requestId;
    private UUID assetId;
    private String assetName;
    private String provider;
    private String treatment;
    private BigDecimal estimateCost;
    private Integer estimateDurationDays;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal actualCost;
    private String reason;
    private String requestorName;
    private String requestorEmail;
    private String type; // "REPAIR" or "SELF_REPAIR"
}