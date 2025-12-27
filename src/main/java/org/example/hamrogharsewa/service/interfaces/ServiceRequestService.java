package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.ServiceRequestCreateDto;
import org.example.hamrogharsewa.dto.response.ServiceRequestResponseDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface ServiceRequestService {

    ServiceRequestResponseDto create(String userId, ServiceRequestCreateDto dto);

    List<ServiceRequestResponseDto> getMyRequestsAsUser(String userId);

    List<ServiceRequestResponseDto> getMyRequestsAsProvider(String providerId);

    void accept(String providerId, String requestId);

    void reject(String providerId, String requestId, String reason);

    void cancel(String userId, String requestId);

    void complete(String actorId, String requestId,
                  Collection<? extends GrantedAuthority> authorities);

    List<ServiceRequestResponseDto> getAllRequests(); // admin
}
