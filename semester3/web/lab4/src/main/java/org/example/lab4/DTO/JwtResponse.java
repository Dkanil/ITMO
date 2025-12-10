package org.example.lab4.DTO;

public class JwtResponse {
    private String access_token;

    public JwtResponse() {}

    public JwtResponse(String access_token) {
        this.access_token = access_token;
    }

    public String getToken() {
        return access_token;
    }
    public void setToken(String access_token) {
        this.access_token = access_token;
    }

}
