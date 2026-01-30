package org.example.hamrogharsewa.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.response.UserResponseDto;
import org.example.hamrogharsewa.exception.ResourceNotFoundException;
import org.example.hamrogharsewa.model.Role;
import org.example.hamrogharsewa.model.User;
import org.example.hamrogharsewa.repository.UserRepository;
import org.example.hamrogharsewa.service.interfaces.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllProviders() {
        return userRepository.findByRole(Role.SERVICE_PROVIDER).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getPendingProviders() {
        return userRepository.findByRoleAndActiveFalse(Role.SERVICE_PROVIDER).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void approveProvider(String id) {
        User user = getUser(id);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void rejectProvider(String id) {
        User user = getUser(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(String id) {
        User user = getUser(id);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(String id) {
        User user = getUser(id);
        user.setActive(false);
        userRepository.save(user);
    }

    // ---------- Helpers ----------

    private User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .active(user.isActive())
                // .createdAt(user.getCreatedAt())
                .build();
    }
}
