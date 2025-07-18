package com.fardhan.assetmanagement.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAssetTypeRequest {
    private String name;
    private UUID categoryId;
}
