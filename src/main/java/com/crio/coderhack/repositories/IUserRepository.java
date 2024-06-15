package com.crio.coderhack.repositories;

import com.crio.coderhack.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
