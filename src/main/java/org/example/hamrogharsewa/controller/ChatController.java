package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.SendMessageRequest;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.ChatMessageDto;
import org.example.hamrogharsewa.service.interfaces.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto<Void>> sendMessage(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody SendMessageRequest request) {

        chatService.sendMessage(
                user.getUsername(),
                request.requestId(),
                request.message(),
                request.receiverId()
        );

        return ResponseEntity.ok(ApiResponseDto.success("Message sent", null));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponseDto<List<ChatMessageDto>>> getChat(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String requestId) {

        List<ChatMessageDto> messages =
                chatService.getChatHistory(requestId, user.getUsername());

        chatService.markAsRead(requestId, user.getUsername());

        return ResponseEntity.ok(
                ApiResponseDto.success("Chat history", messages)
        );
    }

    @GetMapping("/{requestId}/unread-count")
    public ResponseEntity<ApiResponseDto<Long>> unreadCount(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String requestId) {

        return ResponseEntity.ok(
                ApiResponseDto.success(
                        "Unread count",
                        chatService.getUnreadCount(requestId, user.getUsername())
                )
        );
    }
}
