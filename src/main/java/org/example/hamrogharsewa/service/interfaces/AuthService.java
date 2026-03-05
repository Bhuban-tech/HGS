package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.LoginRequestDto;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.LoginResponseDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;

public interface AuthService {

    void registerAndSendOtp(UserRegistrationDto dto);

    LoginResponseDto verifyOtpAndSaveUser(String email, String otp);

    LoginResponseDto login(LoginRequestDto loginRequest);

    void logout(String token);

    LoginResponseDto refreshToken(String refreshToken);
}
