package com.globo.application.controllers;

import com.globo.application.dtos.UserDto;
import com.globo.application.models.UserModel;
import com.globo.application.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    private UserModel userModel;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userModel = UserModel.builder().userId(UUID.randomUUID())
                .email("test@example.com")
                .phoneNumber("123456789")
                .address("Test Address")
                .disabled(false)
                .build();
        userDto = UserDto.builder()
                .email("new@example.com")
                .phoneNumber("987654321")
                .address("rua barao de m")
                .build();
    }

    @Test
    void testGetLogged() {
        when(userService.getLoggedInUsername(request)).thenReturn(userModel);

        ResponseEntity<Object> response = userController.getLogged(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userModel, response.getBody());
    }

    @Test
    void testGetUserById() {
        when(userService.findById(userModel.getUserId())).thenReturn(Optional.of(userModel));

        ResponseEntity<UserModel> response = userController.getUserById(userModel.getUserId());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userModel, response.getBody());
    }

    @Test
    void testDisableAndEnableUser() {
        when(userService.findById(userModel.getUserId())).thenReturn(Optional.of(userModel));

        ResponseEntity<Object> response = userController.disableAndEnableUser(userModel.getUserId());

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).save(any(UserModel.class));
    }

    @Test
    void testUpdateUser() {
        when(userService.findById(userModel.getUserId())).thenReturn(Optional.of(userModel));

        ResponseEntity<Object> response = userController.updateUser(userModel.getUserId(), userDto);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).save(any(UserModel.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userService.findById(userModel.getUserId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.updateUser(userModel.getUserId(), userDto);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testSelfEdit() {
        when(userService.getLoggedInUsername(request)).thenReturn(userModel);

        ResponseEntity<Object> response = userController.selfEdit(userDto, request);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).save(any(UserModel.class));
    }

}