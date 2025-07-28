package com.fardhan.assetmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogActivityResponse {
    private UUID id;
    private String activity;
    private String userName;
    private LocalDateTime createdAt;
}