package org.example.hamrogharsewa.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.LoginRequestDto;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.LoginResponseDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;
import org.example.hamrogharsewa.exception.BadRequestException;
import org.example.hamrogharsewa.exception.UnauthorizedException;
import org.example.hamrogharsewa.model.Role;
import org.example.hamrogharsewa.model.User;
import org.example.hamrogharsewa.repository.UserRepository;
import org.example.hamrogharsewa.service.EmailService;
import org.example.hamrogharsewa.service.interfaces.AuthService;
import org.example.hamrogharsewa.service.store.OtpStore;
import org.example.hamrogharsewa.service.store.Tokenstore;
import org.example.hamrogharsewa.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final Tokenstore tokenStore;
    private final OtpStore otpStore;
    private final EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void registerAndSendOtp(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        try {
            String userData = objectMapper.writeValueAsString(dto);
            otpStore.saveOtpWithUserData(dto.getEmail(), otp, userData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store OTP", e);
        }

        // ✅ SEND EMAIL
        emailService.sendSimpleEmail(
                dto.getEmail(),
                "OTP for Registration",
                "Your OTP is: " + otp + "\nValid for 10 minutes.");
    }

    @Override
    public LoginResponseDto verifyOtpAndSaveUser(String email, String otp) {
        String savedOtp = otpStore.getOtp(email);
        if (savedOtp == null || !savedOtp.equals(otp)) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        try {
            String json = otpStore.getRegistrationData(email);
            UserRegistrationDto dto = objectMapper.readValue(json, UserRegistrationDto.class);

            Role assignedRole = Role.USER;
            if (dto.getRole() != null) {
                if (dto.getRole().equalsIgnoreCase("PROVIDER") || dto.getRole().equalsIgnoreCase("SERVICE_PROVIDER")) {
                    assignedRole = Role.SERVICE_PROVIDER;
                }
            }

            Integer expYears = null;
            if (dto.getExperience() != null && !dto.getExperience().trim().isEmpty()) {
                try {
                    expYears = Integer.parseInt(dto.getExperience().trim());
                } catch (NumberFormatException ignored) {
                }
            }

            User user = User.builder()
                    .userName(dto.getUserName())
                    .email(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .phoneNumber(dto.getPhoneNumber())
                    .role(assignedRole)
                    .address(dto.getAddress())
                    .experienceYears(expYears)
                    .serviceCategoryId(dto.getCategory())
                    .active(true)
                    .build();

            userRepository.save(user);
            otpStore.invalidateRegistration(email);

            String token = jwtUtil.generateToken(
                    user.getId(),
                    user.getEmail(),
                    user.getUserName(),
                    user.getRole().name());

            return new LoginResponseDto(
                    token, user.getId(), user.getUserName(),
                    user.getEmail(), user.getRole());

        } catch (Exception e) {
            throw new RuntimeException("Registration failed", e);
        }
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (tokenStore.isUserBlacklisted(user.getId())) {
            throw new UnauthorizedException("User is suspended");
        }

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().name());

        return new LoginResponseDto(
                token, user.getId(), user.getUserName(),
                user.getEmail(), user.getRole());
    }

    @Override
    public void logout(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        tokenStore.blacklistToken(token, Duration.ofHours(1));
        SecurityContextHolder.clearContext();
    }

    @Override
    public LoginResponseDto refreshToken(String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        if (tokenStore.isBlacklisted(refreshToken)) {
            throw new UnauthorizedException("Token invalidated");
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String email = jwtUtil.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String newToken = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().name());

        return new LoginResponseDto(
                newToken, user.getId(),
                user.getUserName(), user.getEmail(), user.getRole());
    }
}
