package com.studyorganizer.studgroups.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;


    @Value("${jwt.secret}")
    private String jwtSecret;


    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .requireAudience(CLIENT_ID).require("hd", "chnu.edu.ua")
                .verifyWith((SecretKey)getSignInKey()).build()
                .parseSignedClaims(token);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
