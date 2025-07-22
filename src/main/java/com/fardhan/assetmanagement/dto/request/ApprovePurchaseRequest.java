package com.fardhan.assetmanagement.dto.request;

import lombok.Data;

@Data
public class ApprovePurchaseRequest {
    private boolean accepted;
    private String reviewComment;
}