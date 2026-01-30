package org.example.hamrogharsewa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple memory-based message broker to carry the messages back to the
        // client on destinations prefixed with /topic
        config.enableSimpleBroker("/topic", "/queue");
        // Application destination prefix for messages bound for methods annotated with
        // @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
        // For one-to-one messaging
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the endpoint that clients will use to connect to our WebSocket
        // server
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("*"); // In production, replace * with your frontend URL

        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
