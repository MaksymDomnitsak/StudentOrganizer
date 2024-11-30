package com.studyorganizer.userstokens.services;

import com.studmodel.Token;
import com.studmodel.User;
import com.studyorganizer.userstokens.repositories.TokenRepository;
import com.studyorganizer.userstokens.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public Token saveToken(String token, String email, String accessToken, String idToken) {
        Token oAuthToken = new Token();
        oAuthToken.setToken(token);
        oAuthToken.setExpiryDate(Instant.now());
        oAuthToken.setAccessToken(accessToken);
        oAuthToken.setIdToken(idToken);
        User user = userRepository.findByEmail(email);
        if(tokenRepository.findByUser(user).isPresent()) oAuthToken.setId(tokenRepository.findByUser(user).get().getId());
        //token.setId(user.getId());
        oAuthToken.setUserId(user);
        return tokenRepository.save(oAuthToken);
    }

    public Optional<Token> getTokenByUserId(Long userId) {
        return tokenRepository.findById(userId);
    }

    public Optional<Token> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Token getTokenByEmail(String email) {
        User user = userRepository.findByEmail(email);
        Token token = tokenRepository.findByUser(user).orElse(null);
        return token;
    }

    public void updateToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        Token updToken = tokenRepository.findByUserId(user.getId());
        updToken.setToken(token);
        tokenRepository.save(updToken);
    }

    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }
}
