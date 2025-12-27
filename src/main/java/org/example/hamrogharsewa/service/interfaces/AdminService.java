package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.response.UserResponseDto;

import java.util.List;

public interface AdminService {

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getAllProviders();

    List<UserResponseDto> getPendingProviders();

    void approveProvider(String id);

    void rejectProvider(String id);

    void activateUser(String id);

    void deactivateUser(String id);
}
