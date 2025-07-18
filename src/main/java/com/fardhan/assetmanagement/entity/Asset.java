package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "asset")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    public enum AssetStatus {
        UNAVAILABLE,
        AVAILABLE,
        ASSIGNED,
        DAMAGED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_category", nullable = false)
    private AssetCategory assetCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_status", nullable = false)
    private AssetStatus assetStatus;

    @Column(name = "purchase_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal purchasePrice;

    @Column(name = "total_maintenance_price", precision = 15, scale = 2)
    private BigDecimal totalMaintenancePrice;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_holder_id")
    private User currentHolder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Request> requests;
}