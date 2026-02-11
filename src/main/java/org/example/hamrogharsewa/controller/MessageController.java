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

    @MessageMapping("/chat.send")
    public void processMessage(@Payload SendMessageRequest chatMessage, Principal principal) {

        chatService.sendMessage(
                principal.getName(), // sender email/id
                chatMessage.getRequestId(),
                chatMessage.getMessage(),
                chatMessage.getReceiverId());
    }
}
