package com.team3.backend.datafetchers.user;

import com.team3.backend.helpers.UserMailer;
import com.team3.backend.models.User;
import com.team3.backend.repositories.UserRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import org.simplejavamail.MailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public DataFetcher<User> getUserByEmailAndPassword() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String email = (String) input.get("email");
            String password = (String) input.get("password");

            // Hashing logic somewhere in here

            return userRepository.findByEmailAndPassword(email, password);
        };
    }

    public DataFetcher<User> updateUserName() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            String name = dataFetchingEnvironment.getArgument("name");

            User user = userRepository.findByUserId(id);

            if (user == null)
                throw new GraphQLException("No such user!");

            user.setName(name);

            return userRepository.save(user);
        };
    }

    public DataFetcher<User> updateUserEmail() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            String email = dataFetchingEnvironment.getArgument("email");

            if (userRepository.findByEmail(email) != null) {
                throw new GraphQLException("Email already in use!");
            }

            User user = userRepository.findByUserId(id);

            if (user == null)
                throw new GraphQLException("No such user!");

            user.setEmail(email);

            return userRepository.save(user);
        };
    }

    public DataFetcher<User> updateUserPassword() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            String currentPassword = dataFetchingEnvironment.getArgument("currentPassword");
            String newPassword = dataFetchingEnvironment.getArgument("newPassword");

            User user = userRepository.findByUserId(id);

            if (user == null)
                throw new GraphQLException("No such user!");

            if (!user.getPassword().equals(currentPassword))
                throw new GraphQLException("Incorrect password");

            user.setPassword(newPassword);

            return userRepository.save(user);
        };
    }

    public DataFetcher<User> updateUserPreExistingConditions() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            String conditions = dataFetchingEnvironment.getArgument("conditions");

            User user = userRepository.findByUserId(id);

            if (user == null)
                throw new GraphQLException("No such user!");

            user.setPreExistingConditions(conditions);

            return userRepository.save(user);
        };
    }

    public DataFetcher<User> createUser() {
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

    public DataFetcher<Boolean> resetUserPassword() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");
            String password = dataFetchingEnvironment.getArgument("password");

            User user = userRepository.findByEmail(email);

            if (user == null)
                return false;

            user.setPassword(password);
            userRepository.save(user);

            return true;
        };
    }

    public DataFetcher<Boolean> sendPasswordResetCode() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");

            User user = userRepository.findByEmail(email);

            if (user == null) {
                return sendPasswordResetEmail(email, "", "", false);
            }

            // Generate code
            Random codeGenerator = new Random();
            String passwordResetCode = String.valueOf(codeGenerator.nextInt(900000) + 100000);

            // Make sure we are able to successfully send the email to the user before storing the code in the database
            if (sendPasswordResetEmail(email, user.getName(), passwordResetCode, true)) {
                // Save the code and current time to the user
                user.setPasswordResetCode(passwordResetCode);
                user.setPasswordResetTime(LocalDateTime.now().toString());

                userRepository.save(user);
                return true;
            } else
                return false;
        };
    }

    public DataFetcher<Boolean> verifyPasswordResetCode() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");
            String passwordResetCode = dataFetchingEnvironment.getArgument("passwordResetCode");

            User user = userRepository.findByEmail(email);

            System.out.println(user);
            if (user == null)
                return false;

            if (user.getPasswordResetTime().equals(""))
                throw new GraphQLException("Reset code expired, please request a new one.");

            LocalDateTime generationTime = LocalDateTime.parse(user.getPasswordResetTime());
            LocalDateTime currentTime = LocalDateTime.now();

            long elapsedMinutes = java.time.Duration.between(generationTime, currentTime).toMinutes();

            // Code is only valid for 1 hour
            if (elapsedMinutes < 60) {
                if (user.getPasswordResetCode().equals(passwordResetCode)) {
                    // Invalidate the set reset code once the code is used
                    user.setPasswordResetTime("");
                    user.setPasswordResetCode("");
                    userRepository.save(user);
                    return true;
                }

                throw new GraphQLException("Incorrect reset code!");
            } else {
                // Invalidate the set reset code and time if the code has expired
                user.setPasswordResetTime("");
                user.setPasswordResetCode("");
                userRepository.save(user);

                throw new GraphQLException("Reset code expired, please request a new one.");
            }
        };
    }

    private Boolean sendPasswordResetEmail(String email, String name, String passwordResetCode, boolean validUser) {
        if (!validUser) {
            try {
                UserMailer.sendEmail(email, name, "", false);
            } catch (MailException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        try {
            UserMailer.sendEmail(email, name, passwordResetCode, true);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
