package org.example.hamrogharsewa.controller;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.SendMessageRequest;
import org.example.hamrogharsewa.service.interfaces.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * Handles real-time chat messages sent via WebSocket.
     * Destination: /app/chat.send
     */
    @MessageMapping("/chat.send")
    public void processMessage(@Payload SendMessageRequest chatMessage, Principal principal) {
        // 1. Save message to database
        chatService.sendMessage(
                principal.getName(), // sender email/id
                chatMessage.requestId(),
                chatMessage.message(),
                chatMessage.receiverId());

        // 2. Push message to the receiver's private queue
        // Receiver should subscribe to /user/queue/messages
        messagingTemplate.convertAndSendToUser(
                chatMessage.receiverId(),
                "/queue/messages",
                chatMessage);
    }
}
