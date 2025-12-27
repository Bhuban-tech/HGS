package org.example.hamrogharsewa.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String serviceProviderId;
    private String serviceCategoryId;

    @Column(length = 500)
    private String description;

    private String address;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String rejectionReason;

    private LocalDateTime requestedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;

    @PrePersist
    void onCreate() {
        requestedAt = LocalDateTime.now();
        status = RequestStatus.PENDING;
    }
}
