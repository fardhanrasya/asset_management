package com.fardhan.assetmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateAssignRequestRequest {
    @NotNull(message = "Asset ID is required")
    private UUID assetId;

    @NotNull(message = "Target user ID is required")
    private UUID targetUserId;

    private String reason;
}