package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// AssetType Entity
@Entity
@Table(name = "asset_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private AssetCategory category;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Relationships
    @OneToMany(mappedBy = "assetOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asset> assets;
    
    @OneToMany(mappedBy = "assetOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestAssetDetail> requestAssetDetails;
    
    @OneToMany(mappedBy = "assetOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseRequestDetail> purchaseRequestDetails;
}
