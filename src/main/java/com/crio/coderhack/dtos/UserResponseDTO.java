package com.crio.coderhack.dtos;

import com.crio.coderhack.entities.Badge;
import com.crio.coderhack.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    String id;
    String username;
    int score;
    Set<Badge> badges;
}
