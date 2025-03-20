package com.globo.application.controllers;

import com.globo.application.dtos.RecoveryJwtTokenDto;
import com.globo.application.dtos.UserDto;
import com.globo.application.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserDto userDto = UserDto.builder().email("teste@teste.com").username("teste").password("123456").build();
        when(userService.existsByUserName(userDto.getUsername())).thenReturn(true);

        ResponseEntity<Object> response = authenticationController.register(userDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error: Username is Already taken!", response.getBody());
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        UserDto userDto = UserDto.builder().email("teste@teste.com").username("teste").password("123456").build();
        when(userService.existsByUserName(userDto.getUsername())).thenReturn(false);
        when(userService.existsByEmail(userDto.getEmail())).thenReturn(true);

        ResponseEntity<Object> response = authenticationController.register(userDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error: Email is Already taken!", response.getBody());
    }

    @Test
    void testRegisterSuccess() {
        UserDto userDto = UserDto.builder().email("teste@teste.com").username("teste").password("123456").build();
        when(userService.existsByUserName(userDto.getUsername())).thenReturn(false);
        when(userService.existsByEmail(userDto.getEmail())).thenReturn(false);

        ResponseEntity<Object> response = authenticationController.register(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void testSigninSuccess() {
        UserDto userDto = UserDto.builder().email("teste@teste.com").username("teste").password("123456").build();
        RecoveryJwtTokenDto mockToken = new RecoveryJwtTokenDto("mocked-jwt-token");
        when(userService.authenticateUser(userDto)).thenReturn(mockToken);

        ResponseEntity<Object> response = authenticationController.signinUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockToken, response.getBody());
    }
}