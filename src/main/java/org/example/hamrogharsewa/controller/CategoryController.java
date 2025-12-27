package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceCategoryRequestDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.ServiceCategoryResponseDto;
import org.example.hamrogharsewa.service.interfaces.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<ApiResponseDto<ServiceCategoryResponseDto>> create(
            @Valid @RequestBody ServiceCategoryRequestDto dto) {

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Category created",
                        categoryService.create(dto))
        );
    }

    // ✅ ADMIN: Update category
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<ApiResponseDto<ServiceCategoryResponseDto>> update(
            @PathVariable String id,
            @Valid @RequestBody ServiceCategoryRequestDto dto) {

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Category updated",
                        categoryService.update(id, dto))
        );
    }

    // ✅ ADMIN: Soft delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Category deleted", null)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ServiceCategoryResponseDto>>> getAllActive() {
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Active categories",
                        categoryService.getAllActive())
        );
    }
}
