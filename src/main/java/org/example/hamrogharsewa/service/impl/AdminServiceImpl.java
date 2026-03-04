package org.example.hamrogharsewa.service.impl;

import jakarta.transaction.Transactional;
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

    // In AdminServiceImpl.getAllProviders() — add this temporarily
    @Override
    public List<UserResponseDto> getAllProviders() {
        return userRepository.findByRole(Role.SERVICE_PROVIDER)
                .stream()
                .map(UserResponseDto::from)
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
    // In AdminServiceImpl.java — find mapToDto and replace entirely with:
    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.from(user); // ✅ uses the updated from() method
    }
    @Override
    @Transactional
    public void removeProvider(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        userRepository.delete(user);
    }
}
