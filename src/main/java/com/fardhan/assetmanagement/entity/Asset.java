package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "asset")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_option_id", referencedColumnName = "id")
    private AssetType assetOption;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "asset_status", nullable = false)
    private AssetStatus assetStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Relationships
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestAssignDetail> requestAssignDetails;
    
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepairRequestDetail> repairRequestDetails;
    
    public enum AssetStatus {
        UNAVAILABLE("unavailable"),
        AVAILABLE("available");
        
        private final String value;
        
        AssetStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}