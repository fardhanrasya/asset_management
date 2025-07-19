package com.fardhan.assetmanagement.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateRequestAssetRequest {
    private UUID categoryId;
    private Integer quantity;
    private String reason;
}