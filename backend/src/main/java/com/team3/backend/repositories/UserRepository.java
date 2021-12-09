package com.team3.backend.repositories;

import com.team3.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @Author Tony Comanzo, Habib Affinnih
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * @author Tony Comanzo 
     * @param email
     * @return
     */
    @Query("{email:'?0'}")
    User findByEmail(String email);

    /**
     * @author Habib Affinnih
     * @param email
     * @param password
     * @return
     */
    @Query("{email:'?0', password:'?1'}")
    User findByEmailAndPassword(String email, String password);

    /**
     * @author Habib Affinnih
     * @param userId
     * @return
     */
    @Query("{id:'?0'}")
    User findByUserId(String userId);

    /**
     * @author Tony Comanzo 
     */
    long count();
}
