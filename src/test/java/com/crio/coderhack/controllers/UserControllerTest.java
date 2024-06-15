package com.crio.coderhack.controllers;

import com.crio.coderhack.dtos.ScoreRequestDTO;
import com.crio.coderhack.dtos.UserRequestDTO;
import com.crio.coderhack.dtos.UserResponseDTO;
import com.crio.coderhack.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    private UserService userServiceMock;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userServiceMock = mock(UserService.class);
        userController = new UserController(userServiceMock);
    }

    @Test
    void testGetAllUser() {
        // Given
        UserResponseDTO userResponseDTO1 = new UserResponseDTO();
        userResponseDTO1.setId("1");
        userResponseDTO1.setUsername("Alice");
        UserResponseDTO userResponseDTO2 = new UserResponseDTO();
        userResponseDTO2.setId("2");
        userResponseDTO2.setUsername("Bob");
        List<UserResponseDTO> users = Arrays.asList(
                userResponseDTO1, userResponseDTO2
        );
        when(userServiceMock.getUsers()).thenReturn(users);

        // When
        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getAllUser();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }

    @Test
    void testGetUser() {
        // Given
        String userId = "1";
        UserResponseDTO userResponseDTO1 = new UserResponseDTO();
        userResponseDTO1.setId("1");
        userResponseDTO1.setUsername("Alice");
        when(userServiceMock.getUser(userId)).thenReturn(userResponseDTO1);

        // When
        ResponseEntity<UserResponseDTO> responseEntity = userController.getUser(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponseDTO1, responseEntity.getBody());
    }

    @Test
    void testRegisterUser() {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("Alice");
        UserResponseDTO createdUser = new UserResponseDTO();
        createdUser.setId("1");
        createdUser.setUsername("Alice");
        when(userServiceMock.registerUser(userRequestDTO)).thenReturn(createdUser);

        // When
        ResponseEntity<UserResponseDTO> responseEntity = userController.registerUser(userRequestDTO);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(URI.create("/users/" + createdUser.getId()), responseEntity.getHeaders().getLocation());
        assertEquals(createdUser, responseEntity.getBody());
    }

    @Test
    void testRegisterScore() {
        // Given
        String userId = "1";
        ScoreRequestDTO scoreRequestDTO = new ScoreRequestDTO();
        scoreRequestDTO.setScore(100);
        UserResponseDTO updatedUser = new UserResponseDTO();
        updatedUser.setId("1");
        updatedUser.setUsername("Alice");
        updatedUser.setScore(100);
        when(userServiceMock.registerScore(userId, scoreRequestDTO)).thenReturn(updatedUser);

        // When
        ResponseEntity<?> responseEntity = userController.registerScore(userId, scoreRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());
    }

    @Test
    void testDeleteUser() {
        // Given
        String userId = "1";

        // When
        ResponseEntity<String> responseEntity = userController.deleteUser(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User Deleted Successfully!", responseEntity.getBody());
        verify(userServiceMock, times(1)).deleteUser(userId);
    }
}
