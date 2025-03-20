package com.globo.application.controllers;

import com.globo.application.dtos.RecoveryJwtTokenDto;
import com.globo.application.dtos.UserDto;
import com.globo.application.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDto userDto) {

        if(userService.existsByUserName(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already taken!");
        }

        if(userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already taken!");
        }

        userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signinUser(@RequestBody UserDto userDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}
