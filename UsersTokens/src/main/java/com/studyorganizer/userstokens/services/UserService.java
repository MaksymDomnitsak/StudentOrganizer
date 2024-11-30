package com.studyorganizer.userstokens.services;

import com.studmodel.User;
import com.studyorganizer.userstokens.extras.ResourceNotFoundException;
import com.studyorganizer.userstokens.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public Optional<User> getOptionalUserById(Long id){
        return userRepository.findById(id);
    }

    public User getUserById(String id) {
        return getUserById(Long.parseLong(id));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDetails.getEmail());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setPatronymicName(userDetails.getPatronymicName());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setUserRole(userDetails.getUserRole());
            user.setEventer(userDetails.getEventer());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
