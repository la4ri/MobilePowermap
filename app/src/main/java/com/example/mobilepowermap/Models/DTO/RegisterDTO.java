package com.example.mobilepowermap.Models.DTO;

public class RegisterDTO {
    private String email;
    private String password;
    private String nome;
    private String cpfCnpj;
    private String role;

    // Construtor
    public RegisterDTO(String email, String password, String nome, String cpfCnpj, String role) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.role = role;
    }

    // Getters e Setters
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
