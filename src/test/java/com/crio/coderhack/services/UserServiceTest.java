package com.crio.coderhack.services;

import com.crio.coderhack.dtos.ScoreRequestDTO;
import com.crio.coderhack.dtos.UserRequestDTO;
import com.crio.coderhack.dtos.UserResponseDTO;
import com.crio.coderhack.entities.Badge;
import com.crio.coderhack.entities.User;
import com.crio.coderhack.exceptions.InvalidScoreException;
import com.crio.coderhack.exceptions.UserAlreadyExistsException;
import com.crio.coderhack.exceptions.UserNotFoundException;
import com.crio.coderhack.repositories.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper mockModelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userService = new UserService(userRepository, mockModelMapper);
    }

    @Test
    void testGetUser() {
        Long id = 138L;
        User user = new User("username");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(id.toString());
        userResponseDTO.setUsername("username");
        userResponseDTO.setScore(0);
        userResponseDTO.setBadges(Collections.emptySet());
        when(mockModelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        UserResponseDTO result = userService.getUser("138");

        assertEquals("username", result.getUsername());
    }

    @Test
    void testGetUser_UserNotFoundException() {
        // Arrange
        Long id = 48L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUser("48"));
    }

    @Test
    void testGetUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User("username1"));
        users.add(new User("username2"));
        when(userRepository.findAll()).thenReturn(users);
        UserResponseDTO userResponseDTO1 = new UserResponseDTO();
        UserResponseDTO userResponseDTO2 = new UserResponseDTO();
        when(mockModelMapper.map(users.get(0), UserResponseDTO.class)).thenReturn(userResponseDTO1);
        when(mockModelMapper.map(users.get(1), UserResponseDTO.class)).thenReturn(userResponseDTO2);

        // Act
        List<UserResponseDTO> result = userService.getUsers();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(userResponseDTO1));
        assertTrue(result.contains(userResponseDTO2));
    }

    @Test
    void testRegisterUser() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("username");
        User user = new User("username");
        user.setId(1L);
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.save(user)).thenReturn(user);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        when(mockModelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.registerUser(userRequestDTO);

        // Assert
        assertEquals(userResponseDTO, result);
    }

    @Test
    void testRegisterUser_UserAlreadyExistsException() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User("username")));

        // Act and Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userRequestDTO));
    }


    @Test
    void testRegisterScore() {
        // Arrange
        Long id = 1L;
        ScoreRequestDTO scoreRequestDTO = new ScoreRequestDTO();
        scoreRequestDTO.setScore(50);
        User user = new User("username");
        user.setId(id);
        Badge badge = Badge.Code_Champ;
        user.addBadge(badge);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(id.toString());
        userResponseDTO.setUsername("username");
        userResponseDTO.setScore(50);
        userResponseDTO.setBadges(Collections.singleton(badge));
        System.out.println(userResponseDTO.getBadges());
        when(mockModelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.registerScore("1", scoreRequestDTO);

        // Assert
        assertEquals(userResponseDTO, result);
    }

    @Test
    void testRegisterScore_UserNotFoundException() {
        // Arrange
        Long id = 1L;
        ScoreRequestDTO scoreRequestDTO = new ScoreRequestDTO();
        scoreRequestDTO.setScore(50);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userService.registerScore("1", scoreRequestDTO));
    }

    @Test
    void testRegisterScore_InvalidScoreException() {
        Long userId = 1L;
        ScoreRequestDTO scoreRequestDTO = new ScoreRequestDTO();
        scoreRequestDTO.setScore(-1); // Invalid score

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User("username")));

        InvalidScoreException exception = Assertions.assertThrows(InvalidScoreException.class, () -> {
            userService.registerScore(userId.toString(), scoreRequestDTO);
        });

        assertEquals("Score must be between 1 and 100", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteUser_UserFound() {
        Long userId = 1L;
        String userIdString = userId.toString();
        User user = new User("username");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userIdString);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        Long userId = 1L;
        String userIdString = userId.toString();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(userIdString);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}
