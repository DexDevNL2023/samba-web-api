package com.teleo.manager.authentification.security.jwt;

import java.util.Date;

import com.nimbusds.jose.util.Base64;
import com.teleo.manager.authentification.entities.Account;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import com.teleo.manager.generic.utils.AppConstants;
import org.springframework.util.StringUtils;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final long JWT_EXPIRATION_MS = 86400000; // 24 heures
    private static final String base64Secret = "NWMxZTQzYjM2OGExY2ZjYmU3OTMzZmUyMTI2MjVjNDM3NTYyNzc3MDA0ZmFmNjFlZWQ3YmZhYWVhNDAxZjFlMTIwMjdlZDY4MjkwYjhmM2MzNDQyODM5MGE4MzVhNTg2NDk2ZTA4ZmQzZTNlYzU0NGNjN2Y0MjgwY2Q0NmM5YTk=";

    // Generate a SecretKey from the jwtSecret
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(base64Secret).decode();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // parse JWT token from request
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AppConstants.HEADER_PREFIX)) {
            return bearerToken.substring(AppConstants.HEADER_PREFIX.length());
        }
        return null;
    }

    // generate JWT token
    public String generateJwtTokens(Account user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey()) // Use the generated key
                //.signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .claim("id", user.getId())
                .claim("roles", user.getAuthorities())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
