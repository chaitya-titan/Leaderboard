package com.crio.coderhack.repositories;

import com.crio.coderhack.entities.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testFindByUsername_UserExists() {
        // Given
        User user = new User("john_doe");
        user.setId("123");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUsername("john_doe");

        // Then
//        Assertions.assertEquals("john_doe", foundUser.get().getUsername());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");
    }

    @Test
    public void testFindByUsername_UserDoesNotExist() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("jane_doe");

        // Then
        assertThat(foundUser).isNotPresent();
    }
}
