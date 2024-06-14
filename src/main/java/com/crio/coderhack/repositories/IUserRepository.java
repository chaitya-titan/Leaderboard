package com.crio.coderhack.repositories;

import com.crio.coderhack.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends MongoRepository<User, Long> {

}
