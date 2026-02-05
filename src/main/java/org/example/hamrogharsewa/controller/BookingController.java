package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceRequestCreateDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.ServiceRequestResponseDto;
import org.example.hamrogharsewa.service.interfaces.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BookingController {

    private final ServiceRequestService requestService;

    // --- USER ENDPOINTS ---

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto<ServiceRequestResponseDto>> createBooking(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody ServiceRequestCreateDto dto) {

        ServiceRequestResponseDto created = requestService.create(userId, dto);
        return ResponseEntity.ok(ApiResponseDto.success("Booking requested successfully", created));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto<List<ServiceRequestResponseDto>>> getMyBookings(
            @AuthenticationPrincipal String userId) {

        List<ServiceRequestResponseDto> bookings = requestService.getMyRequestsAsUser(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Bookings fetched", bookings));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto<Void>> cancelBooking(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        requestService.cancel(userId, id);
        return ResponseEntity.ok(ApiResponseDto.success("Booking cancelled", null));
    }

    // --- SERVICE PROVIDER ENDPOINTS ---

    @GetMapping("/requests")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ApiResponseDto<List<ServiceRequestResponseDto>>> getServiceRequests(
            @AuthenticationPrincipal String userId) {

        List<ServiceRequestResponseDto> requests = requestService.getMyRequestsAsProvider(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Service requests fetched", requests));
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ApiResponseDto<Void>> acceptBooking(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {

        requestService.accept(userId, id);
        return ResponseEntity.ok(ApiResponseDto.success("Booking accepted", null));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ApiResponseDto<Void>> rejectBooking(
            @AuthenticationPrincipal String userId,
            @PathVariable String id,
            @RequestParam String reason) {

        requestService.reject(userId, id, reason);
        return ResponseEntity.ok(ApiResponseDto.success("Booking rejected", null));
    }

    // --- COMMON ENDPOINT ---

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('USER', 'SERVICE_PROVIDER')")
    public ResponseEntity<ApiResponseDto<Void>> completeBooking(
            Authentication authentication,
            @PathVariable String id) {

        requestService.complete(authentication.getName(), id, authentication.getAuthorities());
        return ResponseEntity.ok(ApiResponseDto.success("Service marked as completed", null));
    }

    // --- ADMIN ENDPOINT ---

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<ApiResponseDto<List<ServiceRequestResponseDto>>> getAllBookings() {
        List<ServiceRequestResponseDto> all = requestService.getAllRequests();
        return ResponseEntity.ok(ApiResponseDto.success("All bookings fetched", all));
    }
}
