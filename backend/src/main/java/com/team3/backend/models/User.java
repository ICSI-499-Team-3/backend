package com.team3.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Tony Comanzo, Habib Affinnih
 */
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private String authToken;

    private String preExistingConditions;

    private String passwordResetCode;

    private String passwordResetTime;

    public User(String id, String name, String email, String password, String authToken) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authToken = authToken;
        this.preExistingConditions = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPreExistingConditions() {
        return preExistingConditions;
    }

    public void setPreExistingConditions(String preExistingConditions) {
        this.preExistingConditions = preExistingConditions;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public String getPasswordResetTime() {
        return passwordResetTime;
    }

    public void setPasswordResetTime(String passwordResetTime) {
        this.passwordResetTime = passwordResetTime;
    }
}
