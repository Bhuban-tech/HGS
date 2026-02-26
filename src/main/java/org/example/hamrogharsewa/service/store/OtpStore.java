package org.example.hamrogharsewa.service.store;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OtpStore {

    private final StringRedisTemplate redisTemplate;

    public static final Duration OTP_EXPIRATION = Duration.ofMinutes(10);
    private static final Duration OTP_VERIFIED_EXPIRATION = Duration.ofMinutes(10);

    public OtpStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    public void saveOtpWithUserData(String email, String otp, String userData) {
        String otpKey = "otp:" + normalizeEmail(email);
        String regKey = "reg:" + normalizeEmail(email);
        redisTemplate.opsForValue().set(otpKey, otp, OTP_EXPIRATION);
        redisTemplate.opsForValue().set(regKey, userData, OTP_EXPIRATION);
    }

    // ✅ fixed — now uses same "otp:" prefix as getOtp
    public void saveOtp(String email, String otp) {
        String key = "otp:" + normalizeEmail(email);
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRATION);
    }

    // ✅ new method for email change OTP
    public void saveEmailChangeOtp(String email, String otp) {
        String key = "otp:" + normalizeEmail(email);
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRATION);
    }

    public String getOtp(String email) {
        String key = "otp:" + normalizeEmail(email);
        return redisTemplate.opsForValue().get(key);
    }

    public String getRegistrationData(String email) {
        String key = "reg:" + normalizeEmail(email);
        return redisTemplate.opsForValue().get(key);
    }

    public void invalidateRegistration(String email) {
        redisTemplate.delete("otp:" + normalizeEmail(email));
        redisTemplate.delete("reg:" + normalizeEmail(email));
    }

    public void invalidateOtp(String email) {
        redisTemplate.delete("otp:" + normalizeEmail(email));
    }

    public void markOtpVerified(String email) {
        String key = "reset:otp-verified:" + normalizeEmail(email);
        redisTemplate.opsForValue().set(key, "true", OTP_VERIFIED_EXPIRATION);
    }

    public boolean isOtpVerified(String email) {
        return redisTemplate.hasKey("reset:otp-verified:" + normalizeEmail(email));
    }

    public void invalidateOtpVerified(String email) {
        redisTemplate.delete("reset:otp-verified:" + normalizeEmail(email));
    }
}