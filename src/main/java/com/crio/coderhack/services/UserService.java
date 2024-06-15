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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final IUserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserService(IUserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponseDTO getUser(String userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User Not Found!");
        }
        return modelMapper.map(user.get(), UserResponseDTO.class);
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();
        users.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        for(User user: users){
            UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
            userResponseDTOS.add(userResponseDTO);
        }
        return userResponseDTOS;
    }

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO){
        Optional<User> existingUser = userRepository.findByUsername(userRequestDTO.getUsername());
        if(existingUser.isPresent()){
            throw new UserAlreadyExistsException("User Already Exists!");
        }
        User user = new User(userRequestDTO.getUsername());
        user.setId(UUID.randomUUID().toString());
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO registerScore(String userId, ScoreRequestDTO scoreRequestDTO){
        Badge badge = getBadge(scoreRequestDTO);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        int score = scoreRequestDTO.getScore();
        if (score < 1 || score > 100) {
            throw new InvalidScoreException("Score must be between 1 and 100");
        }
        user.get().setScore(scoreRequestDTO.getScore());
        user.get().addBadge(badge);
        userRepository.save(user.get());
        return modelMapper.map(user.get(), UserResponseDTO.class);
    }

    public void deleteUser(String userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    private Badge getBadge(ScoreRequestDTO scoreRequestDTO){
        int score = scoreRequestDTO.getScore();
        if(score > 0 && score<30){
            return Badge.Code_Ninja;
        }else if(score >= 30 && score<60){
            return Badge.Code_Champ;
        }else{
            return Badge.Code_Master;
        }
    }

}
