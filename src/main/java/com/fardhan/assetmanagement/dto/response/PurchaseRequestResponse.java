package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PurchaseRequestResponse {
    private UUID requestId;
    private String assetDisplayName;
    private Integer quantity;
    private String status;
    private BigDecimal estimateCost;
    private BigDecimal actualCost;
    private String provider;
    private String reason;
    private String reviewComment;
    private LocalDateTime requestedAt;
    private String requestorName;
    private String requestorEmail;
}