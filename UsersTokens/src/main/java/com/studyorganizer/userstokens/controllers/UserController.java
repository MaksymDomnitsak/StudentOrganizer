package com.studyorganizer.userstokens.controllers;

import com.studmodel.User;
import com.studyorganizer.userstokens.dto.UserDtoRequest;
import com.studyorganizer.userstokens.dto.UserDtoResponse;
import com.studyorganizer.userstokens.extras.ResourceNotFoundException;
import com.studyorganizer.userstokens.mappers.UserToUserDtoMapper;
import com.studyorganizer.userstokens.services.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class UserController {

    private final UserService userService;

    private UserToUserDtoMapper mapper = Mappers.getMapper(UserToUserDtoMapper.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAll() {
        List<UserDtoResponse> userList = new ArrayList<>();
        userService.getAllUsers().forEach(user -> userList.add(mapper.userToDtoUser(user)));
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getOptionalUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserDtoResponse createUser(@RequestBody UserDtoRequest user){
        return mapper.userToDtoUser(userService.createUser(mapper.userDtoToUser(user)));
    }


    @PutMapping("/{id}")
    public UserDtoResponse updateUser(@PathVariable("id") Long id,
                                      @RequestBody UserDtoRequest user){
        return mapper.userToDtoUser(userService.updateUser(id, mapper.userDtoToUser(user)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
