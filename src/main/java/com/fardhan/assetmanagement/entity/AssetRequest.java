package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// AssetRequest Entity
@Entity
@Table(name = "asset_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", referencedColumnName = "id")
    private User requestor;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;
    
    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;
    
    // Relationships
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestAssignDetail> requestAssignDetails;
    
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestAssetDetail> requestAssetDetails;
    
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepairRequestDetail> repairRequestDetails;
    
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseRequestDetail> purchaseRequestDetails;
    
    public enum RequestType {
        REQUEST_ASSET("request_asset"),
        SELF_REPAIR("self_repair"),
        REPAIR("repair"),
        ASSIGN_ASSET("assign_asset"),
        PURCHASE_ASSET("purchase_asset");
        
        private final String value;
        
        RequestType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    public enum RequestStatus {
        OPEN("OPEN"),
        ACCEPTED("ACCEPTED"),
        REJECTED("REJECTED");
        
        private final String value;
        
        RequestStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
