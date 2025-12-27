package org.example.hamrogharsewa.repository;

import org.example.hamrogharsewa.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, String> {

    List<ChatMessage> findByRequestIdOrderByTimestampAsc(String requestId);

    long countByRequestIdAndReceiverIdAndIsReadFalse(String requestId, String receiverId);

    List<ChatMessage> findByRequestIdAndReceiverIdAndIsReadFalse(
            String requestId,
            String receiverId
    );
}
