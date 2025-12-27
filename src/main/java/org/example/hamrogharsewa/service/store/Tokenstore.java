package org.example.hamrogharsewa.service.store;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Tokenstore {

    private final StringRedisTemplate redisTemplate;
    private static final String TOKEN_PREFIX = "blacklisted:";
    private static final String USER_BLACKLIST_PREFIX = "blacklisted:user:";

    public Tokenstore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, Duration duration) {
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, "true", duration);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(TOKEN_PREFIX + token);
    }

    public void blacklistUser(String userId, Duration duration) {
        redisTemplate.opsForValue().set(USER_BLACKLIST_PREFIX + userId, "true", duration);
    }

    public boolean isUserBlacklisted(String userId) {
        return redisTemplate.hasKey(USER_BLACKLIST_PREFIX + userId);
    }

    // 👉 Remove the user from the blacklist
    public void removeUserFromBlacklist(String userId) {
        redisTemplate.delete(USER_BLACKLIST_PREFIX + userId);
    }
}
