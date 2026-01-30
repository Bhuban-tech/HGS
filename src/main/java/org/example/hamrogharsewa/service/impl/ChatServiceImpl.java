package org.example.hamrogharsewa.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.response.ChatMessageDto;
import org.example.hamrogharsewa.exception.ResourceNotFoundException;
import org.example.hamrogharsewa.exception.UnauthorizedException;
import org.example.hamrogharsewa.model.ChatMessage;
import org.example.hamrogharsewa.model.RequestStatus;
import org.example.hamrogharsewa.model.ServiceRequest;
import org.example.hamrogharsewa.repository.ChatRepository;
import org.example.hamrogharsewa.repository.ServiceRequestRepository;
import org.example.hamrogharsewa.service.interfaces.ChatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ServiceRequestRepository requestRepository;

    @Override
    public void sendMessage(String senderId, String requestId, String message, String receiverId) {

        ServiceRequest request = getRequest(requestId);

        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new UnauthorizedException("Chat allowed only after request acceptance");
        }

        validateParticipant(request, senderId);
        validateParticipant(request, receiverId);

        ChatMessage chat = ChatMessage.builder()
                .requestId(requestId)
                .senderId(senderId)
                .receiverId(receiverId)
                .message(message)
                .build();

        chatRepository.save(chat);
    }

    @Override
    public List<ChatMessageDto> getChatHistory(String requestId, String userId) {

        ServiceRequest request = getRequest(requestId);
        validateParticipant(request, userId);

        return chatRepository.findByRequestIdOrderByTimestampAsc(requestId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public void markAsRead(String requestId, String userId) {

        chatRepository.findByRequestIdAndReceiverIdAndIsReadFalse(requestId, userId)
                .forEach(m -> {
                    m.setRead(true);
                    chatRepository.save(m);
                });
    }

    @Override
    public long getUnreadCount(String requestId, String userId) {
        return chatRepository.countByRequestIdAndReceiverIdAndIsReadFalse(requestId, userId);
    }

    // ---------------- HELPERS ----------------

    private ServiceRequest getRequest(String id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));
    }

    private void validateParticipant(ServiceRequest r, String userId) {
        if (!userId.equals(r.getUserId()) && !userId.equals(r.getServiceProviderId())) {
            throw new UnauthorizedException("Not a participant of this request");
        }
    }

    private ChatMessageDto map(ChatMessage m) {
        return new ChatMessageDto(
                m.getId(),
                m.getSenderId(),
                m.getReceiverId(),
                m.getMessage(),
                m.isRead(),
                m.getTimestamp()
        );
    }
}
