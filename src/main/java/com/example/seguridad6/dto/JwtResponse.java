package com.example.seguridad6.dto;

public class JwtResponse {

    private String token;
    private String type = "Bearer";

    public JwtResponse() {
    }

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String token, String type) {
        this.token = token;
        this.type = type;
    }

    // getters & setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
