package com.studyorganizer.googleschedule.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import jakarta.servlet.http.HttpServletRequest;

public class AuthService {
    public static GoogleCredential makeCredentials(HttpServletRequest request){
        String token = request.getHeader("Google");
        return new GoogleCredential().setAccessToken(token);
    }
}
