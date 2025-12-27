package org.example.hamrogharsewa.dto.response;

import java.time.LocalDateTime;

public record ChatMessageDto(
        String id,
        String senderId,
        String receiverId,
        String message,
        boolean isRead,
        LocalDateTime timestamp
) {}
