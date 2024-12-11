package com.example.mobilepowermap.Repositories;

import com.example.mobilepowermap.Models.User;
import com.example.mobilepowermap.APIs.UserAPI;
import com.example.mobilepowermap.Utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository {
    private final UserAPI apiService;

    public UserRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance(null).create(UserAPI.class);
    }

    public void registerUser(User user, Callback<User> callback) {
        Call<User> call = apiService.registerUser(user);
        call.enqueue(callback);
    }

    public void registerAdmin(User user, Callback<User> callback) {
        Call<User> call = apiService.registerAdmin(user);
        call.enqueue(callback);
    }

    public void updateUser(Long userId, User updatedUser, Callback<User> callback) {
        Call<User> call = apiService.updateUser(userId, updatedUser);
        call.enqueue(callback);
    }

    public void deleteUser(Long userId, Callback<Void> callback) {
        Call<Void> call = apiService.deleteUser(userId);
        call.enqueue(callback);
    }
}

