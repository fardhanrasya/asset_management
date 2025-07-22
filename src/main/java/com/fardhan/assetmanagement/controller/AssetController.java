package com.fardhan.assetmanagement.controller;

import com.fardhan.assetmanagement.dto.request.CreateAssetRequest;
import com.fardhan.assetmanagement.dto.request.UpdateAssetRequest;
import com.fardhan.assetmanagement.dto.response.AssetResponse;
import com.fardhan.assetmanagement.entity.User;
import com.fardhan.assetmanagement.repository.UserRepository;
import com.fardhan.assetmanagement.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {
    private final AssetService assetService;
    private final UserRepository userRepository;

    public AssetController(AssetService assetService, UserRepository userRepository) {
        this.assetService = assetService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('HRGA')")
    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(@RequestBody CreateAssetRequest request, Principal principal) {
        // Ambil email dari principal, lalu ambil userId dari repository
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        AssetResponse response = assetService.createAsset(request, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable UUID id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AssetResponse>> getAssetsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(assetService.getAssetsByCategory(categoryId));
    }

    @PreAuthorize("hasRole('HRGA')")
    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAsset(@PathVariable UUID id, @RequestBody UpdateAssetRequest request,
            Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        AssetResponse response = assetService.updateAsset(id, request, user.getId());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('HRGA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable UUID id, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        assetService.deleteAsset(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}