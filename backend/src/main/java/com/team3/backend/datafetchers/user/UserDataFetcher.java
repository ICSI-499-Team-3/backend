package com.team3.backend.datafetchers.user;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class UserDataFetcher {

    private static List<Map<String, String>> users = Arrays.asList(
            ImmutableMap.of(
                    "id", "1",
                    "email", "tcomanzo@albany.edu"
            ),
            ImmutableMap.of(
                    "id", "2",
                    "email", "haffinnih@albany.edu"
            ),
            ImmutableMap.of(
                    "id", "3",
                    "email", "lvelez@albany.edu"
            ),
            ImmutableMap.of(
                    "id", "4",
                    "email", "ewirth@albany.edu"
            )
    );

    public DataFetcher getAllUsers() {
        return dataFetchingEnvironment -> users;
    }

    public DataFetcher getUserByEmail() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");
            return users
                    .stream()
                    .filter(user -> user.get("email").equals(email))
                    .findFirst()
                    .orElse(null);
        };
    }
}
