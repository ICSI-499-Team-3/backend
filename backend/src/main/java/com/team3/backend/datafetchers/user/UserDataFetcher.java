package com.team3.backend.datafetchers.user;

import com.team3.backend.models.User;
import com.team3.backend.repositories.UserRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserDataFetcher {

    private UserRepository userRepository;

    @Autowired
    UserDataFetcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DataFetcher getAllUsers() {
        return dataFetchingEnvironment -> userRepository.findAll();
    }

    public DataFetcher getUserByEmail() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");
            return userRepository.findByEmail(email);
        };
    }

    public DataFetcher createUser() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String email = (String) input.get("email");
            User user = new User(null, email);
            return userRepository.save(user);
        };
    }
}
