package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hamrogharsewa.dto.request.LoginRequestDto;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.LoginResponseDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;
import org.example.hamrogharsewa.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<Void>> register(
            @Valid @RequestBody UserRegistrationDto dto) {

        authService.registerAndSendOtp(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDto.success("OTP sent to email", null));
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "Registration completed",
                        authService.verifyOtpAndSaveUser(email, otp)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(
                ApiResponseDto.success("Login successful", authService.login(request))
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(
            @RequestHeader("Authorization") String token) {

        authService.logout(token);
        return ResponseEntity.ok(ApiResponseDto.success("Logout successful", null));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> refreshToken(
            @RequestParam String refreshToken) {

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "Token refreshed",
                        authService.refreshToken(refreshToken)
                )
        );
    }
}
