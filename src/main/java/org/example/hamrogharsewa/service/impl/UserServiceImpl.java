package org.example.hamrogharsewa.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;
import org.example.hamrogharsewa.exception.EmailSendingException;
import org.example.hamrogharsewa.exception.OtpNotVerifiedException;
import org.example.hamrogharsewa.exception.ResourceNotFoundException;
import org.example.hamrogharsewa.exception.UnauthorizedException;
import org.example.hamrogharsewa.model.User;
import org.example.hamrogharsewa.repository.UserRepository;
import org.example.hamrogharsewa.service.EmailService;
import org.example.hamrogharsewa.service.interfaces.UserService;
import org.example.hamrogharsewa.service.store.OtpStore;
import org.example.hamrogharsewa.utils.OtpGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OtpGenerator otpGenerator;
    private final OtpStore otpStore;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    /*
     * =========================
     * AUTH UTILITY
     * =========================
     */
    private String getCurrentUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        // Since we changed JwtAuthenticationFilter to use userId as principal
        if (principal instanceof String) {
            return principal.toString();
        }

        throw new UnauthorizedException("Invalid authentication principal");
    }

    /*
     * =========================
     * USER PROFILE
     * =========================
     */
    @Override
    public UserResponseDto getCurrentUser() {
        String userId = getCurrentUserIdFromToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserProfile(UserRegistrationDto updatedInfo) {
        String userId = getCurrentUserIdFromToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updatedInfo.getUserName() != null) {
            user.setUserName(updatedInfo.getUserName());
        }

        if (updatedInfo.getPhoneNumber() != null) {
            user.setPhoneNumber(updatedInfo.getPhoneNumber());
        }

        return mapToDto(userRepository.save(user));
    }

    /*
     * =========================
     * PASSWORD CHANGE (LOGGED IN)
     * =========================
     */
    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        String userId = getCurrentUserIdFromToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /*
     * =========================
     * FORGOT PASSWORD FLOW
     * =========================
     */
    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        String otp = otpGenerator.generateOtp();
        otpStore.saveOtp(email, otp);

        String subject = "Password Reset OTP - HamroGharSewa";
        String message = "Hello " + user.getUserName() + ",\n\n" +
                "Your OTP for password reset is:\n\n" +
                otp + "\n\n" +
                "This OTP is valid for 10 minutes.\n\n" +
                "Regards,\nHamroGharSewa Team";

        try {
            emailService.sendSimpleEmail(email, subject, message);
        } catch (Exception ex) {
            log.error("OTP email sending failed", ex);
            throw new EmailSendingException("Failed to send OTP email");
        }
    }

    @Override
    public boolean verifyOtp(String email, String otp) { // include otp parameter
        String storedOtp = otpStore.getOtp(email);

        if (storedOtp == null || !storedOtp.equals(otp)) { // fixed parentheses and variable
            throw new RuntimeException("Invalid or expired OTP");
        }

        otpStore.invalidateOtp(email);
        otpStore.markOtpVerified(email);

        return true;
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) { // added newPassword parameter
        if (!otpStore.isOtpVerified(email)) {
            throw new OtpNotVerifiedException("OTP not verified or expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpStore.invalidateOtpVerified(email);
    }

    @Override
    public java.util.List<UserResponseDto> getProvidersByCategory(String categoryId) {
        return userRepository
                .findByServiceCategoryIdAndRoleAndActiveTrue(categoryId,
                        org.example.hamrogharsewa.model.Role.SERVICE_PROVIDER)
                .stream()
                .map(this::mapToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    /*
     * =========================
     * DTO MAPPER
     * =========================
     */
    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfile(),
                user.getRole() != null ? user.getRole().name() : null,
                user.isActive());
    }
}
