package org.example.hamrogharsewa.dto.response;

import java.time.LocalDateTime;

public class ChatMessageDto {
    private String id;
    private String senderId;
    private String receiverId;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;

    public ChatMessageDto() {}

    public ChatMessageDto(String id, String senderId, String receiverId, String message, boolean isRead, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static ChatMessageDtoBuilder builder() {
        return new ChatMessageDtoBuilder();
    }

    public static class ChatMessageDtoBuilder {
        private String id;
        private String senderId;
        private String receiverId;
        private String message;
        private boolean isRead;
        private LocalDateTime timestamp;

        public ChatMessageDtoBuilder id(String id) { this.id = id; return this; }
        public ChatMessageDtoBuilder senderId(String senderId) { this.senderId = senderId; return this; }
        public ChatMessageDtoBuilder receiverId(String receiverId) { this.receiverId = receiverId; return this; }
        public ChatMessageDtoBuilder message(String message) { this.message = message; return this; }
        public ChatMessageDtoBuilder isRead(boolean isRead) { this.isRead = isRead; return this; }
        public ChatMessageDtoBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ChatMessageDto build() {
            return new ChatMessageDto(id, senderId, receiverId, message, isRead, timestamp);
        }
    }
}
