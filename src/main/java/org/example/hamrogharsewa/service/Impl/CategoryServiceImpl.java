package org.example.hamrogharsewa.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.ServiceCategoryRequestDto;
import org.example.hamrogharsewa.dto.response.ServiceCategoryResponseDto;
import org.example.hamrogharsewa.exception.ResourceNotFoundException;
import org.example.hamrogharsewa.model.ServiceCategory;
import org.example.hamrogharsewa.repository.CategoryRepository;
import org.example.hamrogharsewa.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public ServiceCategoryResponseDto create(ServiceCategoryRequestDto dto) {
        ServiceCategory category = new ServiceCategory();
        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setIcon(dto.icon());
        category.setActive(true);

        return toDto(repository.save(category));
    }

    @Override
    public ServiceCategoryResponseDto update(String id, ServiceCategoryRequestDto dto) {
        ServiceCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setIcon(dto.icon());

        return toDto(repository.save(category));
    }

    @Override
    public void delete(String id) {
        ServiceCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setActive(false);
        repository.save(category);
    }

    @Override
    public List<ServiceCategoryResponseDto> getAllActive() {
        return repository.findByActiveTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ServiceCategoryResponseDto toDto(ServiceCategory category) {
        return ServiceCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .active(category.isActive())
                .build();
    }
}
