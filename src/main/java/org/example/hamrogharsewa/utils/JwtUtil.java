package org.example.hamrogharsewa.utils;

import io.jsonwebtoken.*;
import org.example.hamrogharsewa.exception.InvalidTokenException;
import org.example.hamrogharsewa.exception.TokenExpiredException;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

    public JwtUtil(RsaKeyUtil rsaKeyUtil) throws Exception {
        this.privateKey = rsaKeyUtil.getPrivateKey("src/main/resources/keys/private_key.pem");
        this.publicKey = rsaKeyUtil.getPublicKey("src/main/resources/keys/public_key.pem");
    }

    public String generateToken(String userId, String email, String username, String role) {
        return Jwts.builder()
                .setSubject(email)
                .addClaims(Map.of(
                        "id", userId,
                        "username", username,
                        "role", role
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("JWT expired");
        } catch (JwtException e) {
            throw new InvalidTokenException("Invalid JWT");
        }
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("id", String.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ✅ ADD THIS METHOD
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
