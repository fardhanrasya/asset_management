package com.fardhan.assetmanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// RepairRequestDetail Entity
@Entity
@Table(name = "repair_request_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private AssetRequest request;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Asset asset;
    
    private String provider;
    private String treatment;
    
    @Column(name = "estimate_cost", precision = 10, scale = 2)
    private BigDecimal estimateCost;
    
    @Column(name = "estimate_duration")
    private Integer estimateDuration;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepairStatus status;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost;
    
    public enum RepairStatus {
        PENDING("pending"),
        ONSERVICE("onservice"),
        CANCELLED("cancelled"),
        CLOSED("closed");
        
        private final String value;
        
        RepairStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}