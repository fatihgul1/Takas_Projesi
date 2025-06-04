package com.fatih.takasapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatih.takasapp.entity.User;

public class AuthResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    private User user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
