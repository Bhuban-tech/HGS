package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.response.ChatMessageDto;

import java.util.List;

public interface ChatService {

    void sendMessage(String senderId, String requestId, String message, String receiverId);

    List<ChatMessageDto> getChatHistory(String requestId, String userId);

    void markAsRead(String requestId, String userId);

    long getUnreadCount(String requestId, String userId);
}
