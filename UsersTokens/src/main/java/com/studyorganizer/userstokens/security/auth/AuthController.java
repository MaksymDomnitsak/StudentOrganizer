package com.studyorganizer.userstokens.security.auth;

import com.google.api.client.json.Json;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.studmodel.Token;
import com.studmodel.User;
import com.studyorganizer.userstokens.security.JwtUtils;
import com.studyorganizer.userstokens.services.TokenService;
import com.studyorganizer.userstokens.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;

@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/api/oauth")
public class AuthController {
    @Autowired
    private JwtUtils jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Autowired
    private AuthenticationManager authenticationManager;

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/callback/google")
    public ResponseEntity<?> exchangeCode(String code, String scope) throws GeneralSecurityException, IOException {
        HttpEntity<MultiValueMap<String, String>> requestEntity = getMultiValueMapHttpEntity(code, scope);
        JSONObject response = restTemplate.postForObject(TOKEN_URI, requestEntity, JSONObject.class);
        String token = response.getAsString("id_token");
        String jwtToken = jwtTokenProvider.generateToken(jwtTokenProvider.getEmailFromToken(token), jwtTokenProvider.getRoleFromDB(token));
        String refreshToken = response.getAsString("refresh_token");
        String accessToken = response.getAsString("access_token");
        tokenService.saveToken(refreshToken,jwtTokenProvider.getEmailFromToken(token),accessToken,jwtToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:4200/events"));
        headers.add("Set-Cookie", "Email="+jwtTokenProvider.getEmailFromToken(token)+"; Path=/; Max-Age=300; SameSite=Lax");
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/getCreds")
    public ResponseEntity<?> getCreds(@RequestBody String email) throws GeneralSecurityException, IOException {
        JsonObject json = JsonParser.parseString(email).getAsJsonObject();
        Token token = tokenService.getTokenByEmail(json.get("email").getAsString());
        User user = userService.getUserByEmail(json.get("email").getAsString());
        AuthResponse authResponse = new AuthResponse(user.getId(),user.getEmail(),user.getLastName()+" "+user.getFirstName(),
                user.getEventer(),"Bearer "+token.getIdToken(),token.getAccessToken(),user.getUserRole());
        tokenService.saveToken(token.getToken(),user.getEmail(),"","");
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

//    @GetMapping("/cookieExists")
//    public ResponseEntity<?> cookieExists(HttpServletRequest request) throws GeneralSecurityException, IOException {
//         //перша фігурна дужка і остання
//        Boolean isEmpty = !request.getHeader("Cookie").contains("access_token");
//        return ResponseEntity.ok(isEmpty);
//    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String bearerToken = request.getHeader("Authorization").substring(7);
        String refresh = tokenService.getTokenByUserId(jwtTokenProvider.getUserIDFromUser(bearerToken)).get().getToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refresh);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        JSONObject response = restTemplate.postForObject(TOKEN_URI, entity, JSONObject.class);
        String token = response.getAsString("id_token");
        User user = userService.getUserByEmail(jwtTokenProvider.getEmailFromToken(token));
        String jwtToken = jwtTokenProvider.generateToken(jwtTokenProvider.getEmailFromToken(token), jwtTokenProvider.getRoleFromDB(token));
        String accessToken = response.getAsString("access_token");
        AuthResponse authResponse = new AuthResponse(user.getId(),user.getEmail(),user.getLastName()+" "+user.getFirstName(),
                user.getEventer(),"Bearer "+jwtToken,accessToken,user.getUserRole());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String email = jwtTokenProvider.getEmailFromCustomToken(request.getHeader("Authorization").substring(7));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(httpHeaders);
        String url = "https://oauth2.googleapis.com/revoke?token="+request.getHeader("Revoke");
        String response = restTemplate.postForObject(url, entity, String.class);
        tokenService.updateToken("",email);
        return ResponseEntity.ok("Logged out successfully.");
    }

    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(String code, String scope) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/api/oauth/callback/google");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("scope", scope);
        params.add("grant_type", "authorization_code");

        return new HttpEntity<>(params, httpHeaders);
    }
}



