package org.example.hamrogharsewa.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp() {
        int otp = 100_000 + secureRandom.nextInt(900_000); // 100000-999999
        return String.valueOf(otp);
    }
}
