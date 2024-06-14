package com.crio.coderhack.services;

import com.crio.coderhack.dtos.ScoreRequestDTO;
import com.crio.coderhack.dtos.UserRequestDTO;
import com.crio.coderhack.dtos.UserResponseDTO;
import com.crio.coderhack.entities.Badge;
import com.crio.coderhack.entities.User;
import com.crio.coderhack.repositories.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO: Add Exceptions and checks

    public UserResponseDTO getUser(String userId){
        Long id = Long.parseLong(userId);
        Optional<User> user = userRepository.findById(id);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();
        users.sort((a, b) -> Integer.compare(a.getScore(), b.getScore()));
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        for(User user: users){
            UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
            userResponseDTOS.add(userResponseDTO);
        }
        return userResponseDTOS;
    }

    public User registerUser(UserRequestDTO userRequestDTO){
        User user = new User(userRequestDTO.getUsername());
        long totalUsers = (long) userRepository.findAll().size();
        user.setId(totalUsers+1);
        return userRepository.save(user);
    }

    public void registerScore(String userId, ScoreRequestDTO scoreRequestDTO){
        Long id = Long.parseLong(userId);
        Badge badge = getBadge(scoreRequestDTO);
        Optional<User> user = userRepository.findById(id);
        user.get().setScore(scoreRequestDTO.getScore());
        user.get().addBadge(badge);
        userRepository.save(user.get());
    }

    public void deleteUser(String userId){
        Long id = Long.parseLong(userId);
        userRepository.deleteById(id);
    }

    private Badge getBadge(ScoreRequestDTO scoreRequestDTO){
        //TODO: Write the exception if the score is negative or non usable
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
