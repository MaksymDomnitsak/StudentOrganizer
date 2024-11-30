package com.studyorganizer.userstokens.security;


import com.studmodel.User;
import com.studyorganizer.userstokens.extras.ResourceNotFoundException;
import com.studyorganizer.userstokens.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email);

        return JwtUser.build(user);
    }
}
