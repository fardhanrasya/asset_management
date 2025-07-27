package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AssignRequestResponse {
    private UUID id;
    private UUID assetId;
    private String assetName;
    private String targetUserName;
    private String targetUserEmail;
    private String status;
    private String reason;
    private String reviewComment;
    private LocalDateTime requestedAt;
    private String requestorName;
    private String requestorEmail;
}