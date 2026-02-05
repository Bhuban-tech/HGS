package org.example.hamrogharsewa.config;

import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.utils.JwtUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    if (jwtUtil.validateToken(token)) {
                        String email = jwtUtil.extractEmail(token);
                        String role = jwtUtil.extractRole(token);
                        String userId = jwtUtil.extractUserId(token);

                        if (!role.startsWith("ROLE_")) {
                            role = "ROLE_" + role;
                        }

                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                userId, // Use userId as principal
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );
                        auth.setDetails(email); // Keep email in details if needed
                        accessor.setUser(auth);
                    }
                } catch (Exception e) {
                    // Log error or handle as needed
                }
            }
        }
        return message;
    }
}
