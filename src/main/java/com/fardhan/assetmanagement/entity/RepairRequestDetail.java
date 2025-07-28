package com.fardhan.assetmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RepairRequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    @Column
    private String provider; //tempat asset di service
    
    @Column
    private String treatment; //apa yang diservice
    
    @Column(name = "estimate_cost", precision = 15, scale = 2)
    private BigDecimal estimateCost;
    
    @Column(name = "estimate_duration_days")
    private Integer estimateDurationDays;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepairStatus status;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "actual_cost", precision = 15, scale = 2)
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