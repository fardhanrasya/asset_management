package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

// PurchaseRequestDetail Entity
@Entity
@Table(name = "purchase_request_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;

    @Column(name = "model_name", nullable = false)
    private String modelName;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status;
    
    @Column(name = "estimate_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal estimateCost;
    
    @Column(name = "actual_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal actualCost;
    
    private String provider;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    public enum PurchaseStatus {
        PENDING("PENDING"),
        ACCEPTED("ACCEPTED"),
        REJECTED("REJECTED"),
        ONPURCHASE("ONPURCHASE"),
        CLOSED("CLOSED");
        
        private final String value;
        
        PurchaseStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        } 
    }
}
