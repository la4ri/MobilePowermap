package com.example.mobilepowermap.APIs;

import com.example.mobilepowermap.Models.DTO.RegisterDTO;
import   com.example.mobilepowermap.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {

    @POST("/auth/register")
    Call<Void> registerUser(@Body RegisterDTO registerDTO);
    @POST("/api/user/register-admin")
    Call<User> registerAdmin(@Body User user);

    @PUT("/api/user/{userId}")
    Call<User> updateUser(@Path("userId") Long userId, @Body User updatedUser);

    @DELETE("/api/user/{userId}")
    Call<Void> deleteUser(@Path("userId") Long userId);
}