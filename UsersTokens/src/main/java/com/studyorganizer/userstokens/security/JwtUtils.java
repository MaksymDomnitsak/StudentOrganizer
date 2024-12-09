package com.studyorganizer.userstokens.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Autowired
    private JwtUserDetailsService service;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${StudyOrganizer.app.jwtExpirationMs}")
    private long jwtExpiration;

    public String generateToken(String email, String role, Boolean eventer) {
        return Jwts.builder().audience().add(CLIENT_ID).and().issuer("https://accounts.google.com")
                .subject(email).claim("hd", email.substring(email.indexOf("@")+1))
                .claim("role", role)
                .claim("eventer", eventer)
                .issuedAt(new Date()).expiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .requireAudience(CLIENT_ID).require("hd", "chnu.edu.ua")
                .verifyWith((SecretKey)getSignInKey()).build()
                .parseSignedClaims(token);
    }

    public String getEmailFromToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID)).setIssuer("https://accounts.google.com").build();
        return verifier.verify(token).getPayload().getEmail();
    }

    public String getEmailFromCustomToken(String token){
        Jws<Claims> claims = parseToken(token);
        return claims.getPayload().getSubject();
    }

    public String getRoleFromDB(String token) throws GeneralSecurityException, IOException {
        return service.loadUserByUsername(getEmailFromToken(token)).getAuthorities().toArray()[0].toString();
    }

    public Long getUserIDFromUser(String token) throws GeneralSecurityException, IOException {
        return ((JwtUser) service.loadUserByUsername(getEmailFromCustomToken(token))).getId();
    }

    public Boolean getEventerFromUser(String token) throws GeneralSecurityException, IOException {
        return ((JwtUser) service.loadUserByUsername(getEmailFromToken(token))).getEventer();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
