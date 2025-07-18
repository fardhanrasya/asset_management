package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AssetTypeResponse {
    private UUID id;
    private String name;
    private UUID categoryId;
    private LocalDateTime createdAt;
}
