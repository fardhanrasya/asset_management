package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

// RequestAssetDetail Entity
@Entity
@Table(name = "request_asset_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAssetDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private AssetRequest request;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_option_id", referencedColumnName = "id")
    private AssetType assetOption;
    
    @Column(nullable = false)
    private Integer quantity;
}
