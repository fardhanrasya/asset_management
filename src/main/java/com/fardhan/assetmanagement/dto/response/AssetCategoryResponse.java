package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AssetCategoryResponse {
    private UUID id;
    private String name;
    private UUID parentId;
    private LocalDateTime createdAt;
}
