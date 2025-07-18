package com.fardhan.assetmanagement.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAssetCategoryRequest {
    private String name;
    private UUID parentId;
}
