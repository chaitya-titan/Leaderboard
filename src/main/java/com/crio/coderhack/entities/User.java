package com.crio.coderhack.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "user")
public class User extends BaseEntity{
    private String username;
    private int score;
    private Set<Badge> badges;

    public User(String username){
        this.username = username;
        this.score = 0;
        this.badges = new HashSet<>();
    }

    public void addBadge(Badge badge){
        this.badges.add(badge);
    }

}
