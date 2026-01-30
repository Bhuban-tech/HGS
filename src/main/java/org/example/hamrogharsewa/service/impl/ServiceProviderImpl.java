// ✅ CORRECT
package org.example.hamrogharsewa.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceRequestCreateDto;
import org.example.hamrogharsewa.dto.response.ServiceRequestResponseDto;
import org.example.hamrogharsewa.exception.ResourceNotFoundException;
import org.example.hamrogharsewa.exception.UnauthorizedException;
import org.example.hamrogharsewa.model.RequestStatus;
import org.example.hamrogharsewa.model.ServiceRequest;
import org.example.hamrogharsewa.repository.ServiceRequestRepository;
import org.example.hamrogharsewa.service.interfaces.ServiceRequestService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProviderImpl implements ServiceRequestService {

    private final ServiceRequestRepository repository;

    @Override
    public ServiceRequestResponseDto create(String userId, ServiceRequestCreateDto dto) {

        ServiceRequest request = ServiceRequest.builder()
                .userId(userId)
                .serviceProviderId(dto.getServiceProviderId())
                .serviceCategoryId(dto.getServiceCategoryId())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .status(RequestStatus.PENDING)
                .build();

        return map(repository.save(request));
    }

    @Override
    public List<ServiceRequestResponseDto> getMyRequestsAsUser(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceRequestResponseDto> getMyRequestsAsProvider(String providerId) {
        return repository.findByServiceProviderId(providerId).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public void accept(String providerId, String requestId) {

        ServiceRequest req = get(requestId);

        if (!req.getServiceProviderId().equals(providerId)) {
            throw new UnauthorizedException("Not your request");
        }

        req.setStatus(RequestStatus.ACCEPTED);
        req.setAcceptedAt(LocalDateTime.now());
        repository.save(req);
    }

    @Override
    public void reject(String providerId, String requestId, String reason) {

        ServiceRequest req = get(requestId);

        if (!req.getServiceProviderId().equals(providerId)) {
            throw new UnauthorizedException("Not your request");
        }

        req.setStatus(RequestStatus.REJECTED);
        req.setRejectionReason(reason);
        repository.save(req);
    }

    @Override
    public void cancel(String userId, String requestId) {

        ServiceRequest req = get(requestId);

        if (!req.getUserId().equals(userId)) {
            throw new UnauthorizedException("Not your booking");
        }

        if (req.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be cancelled");
        }

        req.setStatus(RequestStatus.REJECTED);
        repository.save(req);
    }

    @Override
    public void complete(String actorId, String requestId,
            Collection<? extends GrantedAuthority> authorities) {

        ServiceRequest req = get(requestId);

        boolean isUser = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
                && req.getUserId().equals(actorId);

        boolean isProvider = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SERVICE_PROVIDER"))
                && req.getServiceProviderId().equals(actorId);

        if (!isUser && !isProvider) {
            throw new UnauthorizedException("Not authorized");
        }

        req.setStatus(RequestStatus.COMPLETED);
        req.setCompletedAt(LocalDateTime.now());
        repository.save(req);
    }

    @Override
    public List<ServiceRequestResponseDto> getAllRequests() {
        return repository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private ServiceRequest get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
    }

    private ServiceRequestResponseDto map(ServiceRequest r) {
        return ServiceRequestResponseDto.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .serviceProviderId(r.getServiceProviderId())
                .serviceCategoryId(r.getServiceCategoryId())
                .description(r.getDescription())
                .address(r.getAddress())
                .status(r.getStatus().name())
                .rejectionReason(r.getRejectionReason())
                .requestedAt(r.getRequestedAt())
                .acceptedAt(r.getAcceptedAt())
                .completedAt(r.getCompletedAt())
                .build();
    }
}
