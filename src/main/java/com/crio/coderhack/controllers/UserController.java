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
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO){
        User user = userService.registerUser(userRequestDTO);
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body("Created");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> registerScore(@PathVariable String userId, @RequestBody ScoreRequestDTO scoreRequestDTO){
        userService.registerScore(userId, scoreRequestDTO);
        return ResponseEntity.ok().body("Updated");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("User Deleted");
    }

}
