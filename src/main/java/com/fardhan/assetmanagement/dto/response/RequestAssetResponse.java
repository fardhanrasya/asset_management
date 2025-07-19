package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RequestAssetResponse {
    private UUID id;
    private UUID categoryId;
    private String categoryName;
    private Integer quantity;
    private String status;
    private String reason;
    private String reviewComment;
    private LocalDateTime requestedAt;
    private String requestorName;
    private String requestorEmail;
}