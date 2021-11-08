package com.team3.backend.datafetchers.user;

import com.team3.backend.models.User;
import com.team3.backend.repositories.UserRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class UserDataFetcher {

    private UserRepository userRepository;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    @Autowired
    UserDataFetcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DataFetcher<List<User>> getAllUsers() {
        return dataFetchingEnvironment -> userRepository.findAll();
    }

    public DataFetcher<User> getUserByEmail() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");
            return userRepository.findByEmail(email);
        };
    }

    public DataFetcher createUser() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String email = (String) input.get("email");

            if (userRepository.findByEmail(email) != null) {
                throw new GraphQLException("User already exists!");
            }

            String name = (String) input.get("name");
            String password = (String) input.get("password");
            String authToken = generateNewToken();
            User user = new User(null, name, email, password, authToken);

            return userRepository.save(user);
        };
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
