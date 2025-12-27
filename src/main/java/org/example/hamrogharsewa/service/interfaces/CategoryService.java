package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.ServiceCategoryRequestDto;
import org.example.hamrogharsewa.dto.response.ServiceCategoryResponseDto;

import java.util.List;

public interface CategoryService {

    ServiceCategoryResponseDto create(ServiceCategoryRequestDto dto);

    ServiceCategoryResponseDto update(String id, ServiceCategoryRequestDto dto);

    void delete(String id);

    List<ServiceCategoryResponseDto> getAllActive();
}
