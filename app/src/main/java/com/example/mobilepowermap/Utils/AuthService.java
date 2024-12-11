package com.example.mobilepowermap.Utils;

import com.example.mobilepowermap.Models.DTO.AuthenticationDTO;
import com.example.mobilepowermap.Models.DTO.LoginResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth/login")
    Call<LoginResponseDTO> login(@Body AuthenticationDTO authData);
}
