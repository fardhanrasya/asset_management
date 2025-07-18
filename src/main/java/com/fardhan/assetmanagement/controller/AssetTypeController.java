package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreateAssetTypeRequest;
import com.fardhan.assetmanagement.dto.response.AssetTypeResponse;
import com.fardhan.assetmanagement.entity.AssetType;
import com.fardhan.assetmanagement.service.AssetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/asset-types")
@RequiredArgsConstructor
public class AssetTypeController {

    private final AssetTypeService assetTypeService;

    @PostMapping
    public ResponseEntity<AssetTypeResponse> create(@RequestBody CreateAssetTypeRequest request) {
        AssetType assetType = assetTypeService.create(request);
        AssetTypeResponse response = AssetTypeResponse.builder()
                .id(assetType.getId())
                .name(assetType.getName())
                .categoryId(assetType.getCategory().getId())
                .createdAt(assetType.getCreatedAt())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<java.util.List<AssetTypeResponse>> getAll() {
        java.util.List<AssetTypeResponse> responses = assetTypeService.findAll().stream()
                .map(assetType -> AssetTypeResponse.builder()
                        .id(assetType.getId())
                        .name(assetType.getName())
                        .categoryId(assetType.getCategory().getId())
                        .createdAt(assetType.getCreatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetTypeResponse> getById(@PathVariable java.util.UUID id) {
        return assetTypeService.findById(id)
                .map(assetType -> AssetTypeResponse.builder()
                        .id(assetType.getId())
                        .name(assetType.getName())
                        .categoryId(assetType.getCategory().getId())
                        .createdAt(assetType.getCreatedAt())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
