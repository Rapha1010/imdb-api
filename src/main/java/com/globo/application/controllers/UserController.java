package com.globo.application.controllers;

import com.globo.application.dtos.UserDto;
import com.globo.application.models.UserModel;
import com.globo.application.services.UserService;
import com.globo.application.specifications.UserSpecification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/my-account")
    public ResponseEntity<Object> getLogged(HttpServletRequest request) {
        UserModel loggedUser = userService.getLoggedInUsername(request);
        return ResponseEntity.ok().body(loggedUser);
    }

    @GetMapping
    public Page<UserModel> getUserList(UserSpecification spec, @PageableDefault(size = 10, sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.findAll(spec, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(userService.findById(id).get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> disableAndEnableUser(@PathVariable UUID id) {
        var userModel = userService.findById(id).get();

        userModel.setDisabled(!userModel.getDisabled());
        userService.save(userModel);

        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        Optional<UserModel> userModelOptional = userService.findById(id);

        if (userModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        var userModel = userModelOptional.get();

        System.out.println(userModel);

        userModel.setEmail(userDto.getEmail());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setAddress(userDto.getAddress());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        return  ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/self-edit")
    public ResponseEntity<Object> selfEdit(@RequestBody UserDto userDto, HttpServletRequest request) {

        var loggedUser = userService.getLoggedInUsername(request);

        Optional<UserModel> userModelOptional = Optional.of(loggedUser);

        var userModel = userModelOptional.get();
        userModel.setEmail(userDto.getEmail());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setAddress(userDto.getAddress());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        return  ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

}
