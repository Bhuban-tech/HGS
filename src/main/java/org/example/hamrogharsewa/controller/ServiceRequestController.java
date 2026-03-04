package org.example.hamrogharsewa.controller;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceRequestCreateDto;
import org.example.hamrogharsewa.dto.response.ServiceRequestResponseDto;
import org.example.hamrogharsewa.service.interfaces.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/service-requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    // ─────────────────────────────────────────────────────────────
    // POST /api/service-requests
    // USER creates a new booking request
    // ─────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ServiceRequestResponseDto> createRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ServiceRequestCreateDto dto) {

        String userId = userDetails.getUsername(); // or extract from JWT
        ServiceRequestResponseDto response = serviceRequestService.create(userId, dto);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/service-requests/my
    // USER sees their own submitted requests
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/my")
    public ResponseEntity<List<ServiceRequestResponseDto>> getMyRequestsAsUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = userDetails.getUsername();
        return ResponseEntity.ok(serviceRequestService.getMyRequestsAsUser(userId));
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/service-requests/provider
    // PROVIDER sees requests assigned to them
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/provider")
    public ResponseEntity<List<ServiceRequestResponseDto>> getMyRequestsAsProvider(
            @AuthenticationPrincipal UserDetails userDetails) {

        String providerId = userDetails.getUsername();
        return ResponseEntity.ok(serviceRequestService.getMyRequestsAsProvider(providerId));
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/service-requests/all
    // ADMIN sees all requests
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/all")
    public ResponseEntity<List<ServiceRequestResponseDto>> getAllRequests() {
        return ResponseEntity.ok(serviceRequestService.getAllRequests());
    }

    // ─────────────────────────────────────────────────────────────
    // PUT /api/service-requests/{id}/accept
    // PROVIDER accepts a request
    // ─────────────────────────────────────────────────────────────
    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {

        String providerId = userDetails.getUsername();
        serviceRequestService.accept(providerId, id);
        return ResponseEntity.ok().build();
    }

    // ─────────────────────────────────────────────────────────────
    // PUT /api/service-requests/{id}/reject
    // PROVIDER rejects a request with a reason
    // ─────────────────────────────────────────────────────────────
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String providerId = userDetails.getUsername();
        String reason = body.getOrDefault("reason", "No reason provided");
        serviceRequestService.reject(providerId, id, reason);
        return ResponseEntity.ok().build();
    }

    // ─────────────────────────────────────────────────────────────
    // PUT /api/service-requests/{id}/cancel
    // USER cancels their own PENDING request
    // ─────────────────────────────────────────────────────────────
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {

        String userId = userDetails.getUsername();
        serviceRequestService.cancel(userId, id);
        return ResponseEntity.ok().build();
    }

    // ─────────────────────────────────────────────────────────────
    // PUT /api/service-requests/{id}/complete
    // USER or PROVIDER marks the request as complete
    // ─────────────────────────────────────────────────────────────
    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {

        String actorId = userDetails.getUsername();
        serviceRequestService.complete(actorId, id, userDetails.getAuthorities());
        return ResponseEntity.ok().build();
    }
}