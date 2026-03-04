package org.example.hamrogharsewa.service.impl;

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
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIcon(dto.getIcon());
        category.setActive(true);

        return mapToDto(repository.save(category));
    }

    @Override
    public ServiceCategoryResponseDto update(String id, ServiceCategoryRequestDto dto) {
        ServiceCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIcon(dto.getIcon());

        return mapToDto(repository.save(category));
    }

    @Override
    public void delete(String id) {
        ServiceCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setActive(false); // soft delete
        repository.save(category);
    }

    @Override
    public List<ServiceCategoryResponseDto> getAllActive() {
        return repository.findByActiveTrue()
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    private ServiceCategoryResponseDto mapToDto(ServiceCategory category) {
        return ServiceCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .active(category.isActive())
                .build();
    }
}
