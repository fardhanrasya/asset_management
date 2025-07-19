package com.fardhan.assetmanagement.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateRequestStatusRequest {
    private UUID requestId;
    private String status; // ACCEPTED or REJECTED
    private String reviewComment;
}