package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceCategoryRequestDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.ServiceCategoryResponseDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;
import org.example.hamrogharsewa.service.interfaces.AdminService;
import org.example.hamrogharsewa.service.interfaces.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final CategoryService categoryService;



    @GetMapping("/users")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "All users fetched", adminService.getAllUsers())
        );
    }

    @GetMapping("/service-providers")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllProviders() {
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "All service providers fetched", adminService.getAllProviders())
        );
    }

    @GetMapping("/service-providers/pending")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getPendingProviders() {
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Pending providers fetched", adminService.getPendingProviders())
        );
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<ApiResponseDto<Void>> approveProvider(@PathVariable String id) {
        adminService.approveProvider(id);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Provider approved", null));
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<ApiResponseDto<Void>> rejectProvider(@PathVariable String id) {
        adminService.rejectProvider(id);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Provider rejected", null));
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<ApiResponseDto<Void>> activateUser(@PathVariable String id) {
        adminService.activateUser(id);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "User activated", null));
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deactivateUser(@PathVariable String id) {
        adminService.deactivateUser(id);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "User deactivated", null));
    }



    @PostMapping("/categories")
    public ResponseEntity<ApiResponseDto<ServiceCategoryResponseDto>> createCategory(
            @Valid @RequestBody ServiceCategoryRequestDto dto) {

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Category created", categoryService.create(dto))
        );
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponseDto<ServiceCategoryResponseDto>> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody ServiceCategoryRequestDto dto) {

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Category updated", categoryService.update(id, dto))
        );
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Category deactivated", null));
    }
}
