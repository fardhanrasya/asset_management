package com.fardhan.assetmanagement.dto.request;

import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateRepairStatusRequest {
    private UUID repairRequestDetailId;
    private String status; // ONSERVICE, CLOSED, etc.
    private String provider;
    private LocalDate startDate;
    private BigDecimal actualCost;
    private String treatment;
    private LocalDate endDate;
    private String warranty;
}