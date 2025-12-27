package org.example.hamrogharsewa.service.store;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OtpStore {

    private final StringRedisTemplate redisTemplate;

    // OTP expiration time
    private static final Duration OTP_EXPIRATION = Duration.ofMinutes(10);
    private static final Duration OTP_VERIFIED_EXPIRATION = Duration.ofMinutes(10);

    public OtpStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Helper to normalize email
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    /**
     * Save OTP for password reset
     */
    public void saveOtp(String email, String otp) {
        String key = "reset:otp:" + normalizeEmail(email);
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRATION);
    }

    /**
     * Save OTP along with registration data
     */
    public void saveOtpWithUserData(String email, String otp, String userData) {
        String otpKey = "otp:" + normalizeEmail(email);
        String regKey = "reg:" + normalizeEmail(email);

        redisTemplate.opsForValue().set(otpKey, otp, OTP_EXPIRATION);
        redisTemplate.opsForValue().set(regKey, userData, OTP_EXPIRATION);
    }

    /**
     * Get OTP for registration
     */
    public String getOtp(String email) {
        String key = "otp:" + normalizeEmail(email);
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Get registration data
     */
    public String getRegistrationData(String email) {
        String key = "reg:" + normalizeEmail(email);
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Invalidate OTP and registration data after successful registration
     */
    public void invalidateRegistration(String email) {
        redisTemplate.delete("otp:" + normalizeEmail(email));
        redisTemplate.delete("reg:" + normalizeEmail(email));
    }

    /**
     * Invalidate OTP only
     */
    public void invalidateOtp(String email) {
        redisTemplate.delete("otp:" + normalizeEmail(email));
    }

    /**
     * Save OTP verified flag
     */
    public void markOtpVerified(String email) {
        String key = "reset:otp-verified:" + normalizeEmail(email);
        redisTemplate.opsForValue().set(key, "true", OTP_VERIFIED_EXPIRATION);
    }

    /**
     * Check if OTP is verified
     */
    public boolean isOtpVerified(String email) {
        return redisTemplate.hasKey("reset:otp-verified:" + normalizeEmail(email));
    }

    /**
     * Invalidate OTP verified flag
     */
    public void invalidateOtpVerified(String email) {
        redisTemplate.delete("reset:otp-verified:" + normalizeEmail(email));
    }
}
