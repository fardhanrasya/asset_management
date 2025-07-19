package com.fardhan.assetmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PurchaseRequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;
    
    @Column(name = "asset_display_name")
    private String assetDisplayName;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status;
    
    @Column(name = "estimate_cost", precision = 15, scale = 2)
    private BigDecimal estimateCost;
    
    @Column(name = "actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost;
    
    @Column
    private String provider;

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
