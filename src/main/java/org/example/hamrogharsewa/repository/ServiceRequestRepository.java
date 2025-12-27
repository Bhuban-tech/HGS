package org.example.hamrogharsewa.repository;

import org.example.hamrogharsewa.model.RequestStatus;
import org.example.hamrogharsewa.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, String> {

    List<ServiceRequest> findByUserId(String userId);

    List<ServiceRequest> findByServiceProviderId(String providerId);

    List<ServiceRequest> findByStatus(RequestStatus status);
}
