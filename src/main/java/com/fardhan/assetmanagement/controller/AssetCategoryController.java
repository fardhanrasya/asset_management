package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreateAssetCategoryRequest;
import com.fardhan.assetmanagement.dto.response.AssetCategoryResponse;
import com.fardhan.assetmanagement.entity.AssetCategory;
import com.fardhan.assetmanagement.service.AssetCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/asset-categories")
@RequiredArgsConstructor
public class AssetCategoryController {

        private final AssetCategoryService assetCategoryService;

        @PostMapping
        public ResponseEntity<AssetCategoryResponse> create(@RequestBody CreateAssetCategoryRequest request) {
                AssetCategory assetCategory = assetCategoryService.create(request);
                AssetCategoryResponse response = AssetCategoryResponse.builder()
                                .id(assetCategory.getId())
                                .name(assetCategory.getName())
                                .parentId(assetCategory.getParent() != null ? assetCategory.getParent().getId() : null)
                                .createdAt(assetCategory.getCreatedAt())
                                .build();
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @GetMapping
        public ResponseEntity<java.util.List<AssetCategoryResponse>> getAll() {
                java.util.List<AssetCategoryResponse> responses = assetCategoryService.findAll().stream()
                                .map(assetCategory -> AssetCategoryResponse.builder()
                                                .id(assetCategory.getId())
                                                .name(assetCategory.getName())
                                                .parentId(assetCategory.getParent() != null
                                                                ? assetCategory.getParent().getId()
                                                                : null)
                                                .createdAt(assetCategory.getCreatedAt())
                                                .build())
                                .toList();
                return ResponseEntity.ok(responses);
        }

        @GetMapping("/{id}")
        public ResponseEntity<AssetCategoryResponse> getById(@PathVariable java.util.UUID id) {
                return assetCategoryService.findById(id)
                                .map(assetCategory -> AssetCategoryResponse.builder()
                                                .id(assetCategory.getId())
                                                .name(assetCategory.getName())
                                                .parentId(assetCategory.getParent() != null
                                                                ? assetCategory.getParent().getId()
                                                                : null)
                                                .createdAt(assetCategory.getCreatedAt())
                                                .build())
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }
}
