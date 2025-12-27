package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceRequestCreateDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.ServiceRequestResponseDto;
import org.example.hamrogharsewa.service.interfaces.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ServiceProviderController {

    private final ServiceRequestService requestService;

    // USER: Create booking
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto<ServiceRequestResponseDto>> createBooking(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ServiceRequestCreateDto dto) {

        ServiceRequestResponseDto created =
                requestService.create(user.getUsername(), dto);

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Service request created", created)
        );
    }

    // USER: View own bookings
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto<List<ServiceRequestResponseDto>>> getMyBookings(
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                new ApiResponseDto<>(
                        true,
                        "Your bookings",
                        requestService.getMyRequestsAsUser(user.getUsername())
                )
        );
    }


    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto<?>> cancelBooking(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String id) {

        requestService.cancel(user.getUsername(), id);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Booking cancelled", null)
        );
    }


    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('USER','SERVICE_PROVIDER')")
    public ResponseEntity<ApiResponseDto<?>> completeBooking(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String id) {

        requestService.complete(user.getUsername(), id, user.getAuthorities());
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Service completed", null)
        );
    }
}
