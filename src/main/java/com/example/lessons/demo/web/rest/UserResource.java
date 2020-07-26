package com.example.lessons.demo.web.rest;

import com.example.lessons.demo.domain.User;
import com.example.lessons.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;


    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody User user) {
        if (!checkForPasswordLength(user.getPassword())) {
            return new ResponseEntity("Parol uchunligi 4 dan kam", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkUserName(user.getUserName())) {
            return new ResponseEntity("User oldin ro'yxatdan otgan", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.create(user));
    }

    private Boolean checkForPasswordLength(String password) {
        return password.length() >= 4;
    }

}
