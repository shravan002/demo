package com.agbeindia.demo.controllers;

import com.agbeindia.demo.entities.User;
import com.agbeindia.demo.entities.UserResponse;
import com.agbeindia.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userservice")
public class UserController {

    @Autowired
    private UserService userService;

    private final String CODE = "CODE";
    private final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    private final String DESCRIPTION_KEY = "description";
    private final String DESCRIPTION = "A user with the given username already exists";

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            UserResponse createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(createdUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put(CODE, USER_ALREADY_EXISTS);
            response.put(DESCRIPTION_KEY, DESCRIPTION);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
