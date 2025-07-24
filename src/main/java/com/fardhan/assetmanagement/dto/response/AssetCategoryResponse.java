package com.fardhan.assetmanagement.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class AssetCategoryResponse {
    UUID id;
    String name;
    UUID parentId;
    LocalDateTime createdAt;
}
