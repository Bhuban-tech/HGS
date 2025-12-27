package org.example.hamrogharsewa.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ServiceRequestResponseDto {

    private String id;
    private String userId;
    private String serviceProviderId;
    private String serviceCategoryId;
    private String description;
    private String address;
    private String status;
    private String rejectionReason;
    private LocalDateTime requestedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
}
