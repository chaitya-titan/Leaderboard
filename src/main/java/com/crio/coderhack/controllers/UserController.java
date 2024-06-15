package com.crio.coderhack.controllers;

import com.crio.coderhack.dtos.ScoreRequestDTO;
import com.crio.coderhack.dtos.UserRequestDTO;
import com.crio.coderhack.dtos.UserResponseDTO;
import com.crio.coderhack.entities.User;
import com.crio.coderhack.exceptions.InvalidScoreException;
import com.crio.coderhack.exceptions.UserAlreadyExistsException;
import com.crio.coderhack.exceptions.UserNotFoundException;
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
        try{
            UserResponseDTO userResponseDTO = userService.getUser(userId);
            return ResponseEntity.ok().body(userResponseDTO);
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO){
        try {
            UserResponseDTO userResponseDTO = userService.registerUser(userRequestDTO);
            return ResponseEntity.created(URI.create("/users/" + userResponseDTO.getId())).body(userResponseDTO);
        } catch (UserAlreadyExistsException ex){
            return ResponseEntity.status(409).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> registerScore(@PathVariable String userId,
                                                         @RequestBody ScoreRequestDTO scoreRequestDTO)
    throws RuntimeException{
        try {
            UserResponseDTO updatedUserResponseDTO = userService.registerScore(userId, scoreRequestDTO);
            return ResponseEntity.ok(updatedUserResponseDTO);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (InvalidScoreException ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User Deleted Successfully!");
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

}
