package com.example.mobilepowermap.Models.DTO;

public class LoginResponseDTO {
    private String token;
    private String role;

    public LoginResponseDTO(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}
