package com.studyorganizer.events.security;


import com.studmodel.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    RestTemplate restTemplate = new RestTemplate();
    @Override
    public UserDetails loadUserByUsername(String email) {

        return null;
    }

    @Transactional
    public UserDetails loadUserByUsername(String email,String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("Authorization", token);
        httpHeaders.set("Authorization", "Bearer " + token);
        User user =  restTemplate.getForObject("localhost:8080://api/users/"+email,User.class);
        return JwtUser.build(user);
    }
}
