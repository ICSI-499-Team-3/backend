package com.team3.backend.repositories;

import com.team3.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{email:'?0'}")
    User findByEmail(String email);

    @Query("{email:'?0', password:'?1'}")
    User findByEmailAndPassword(String email, String password);

    @Query("{id:'?0'}")
    User findByUserId(String userId);

    long count();
}
