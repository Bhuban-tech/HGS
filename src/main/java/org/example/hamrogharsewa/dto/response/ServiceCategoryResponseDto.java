package org.example.hamrogharsewa.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryResponseDto {
    private String id;
    private String name;
    private String description;
    private String icon;
    private boolean active;
}
