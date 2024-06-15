package com.crio.coderhack.controllers;

import com.crio.coderhack.dtos.ScoreRequestDTO;
import com.crio.coderhack.dtos.UserRequestDTO;
import com.crio.coderhack.dtos.UserResponseDTO;
import com.crio.coderhack.entities.User;
import com.crio.coderhack.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> getAllUser(){
        List<UserResponseDTO> userResponseDTOS = userService.getUsers();
        return ResponseEntity.ok().body(userResponseDTOS);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String userId){
        UserResponseDTO userResponseDTO = userService.getUser(userId);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = userService.registerUser(userRequestDTO);
        return ResponseEntity.created(URI.create("/users/" + userResponseDTO.getId())).body(userResponseDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> registerScore(@PathVariable String userId, @RequestBody ScoreRequestDTO scoreRequestDTO){
        UserResponseDTO updatedUserResponseDTO = userService.registerScore(userId, scoreRequestDTO);
        return ResponseEntity.ok(updatedUserResponseDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully!");
    }

}
