package org.example.hamrogharsewa.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(
        @NotBlank String requestId,
        @NotBlank String message,
        @NotBlank String receiverId
) {}
