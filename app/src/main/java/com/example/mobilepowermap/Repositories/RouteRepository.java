package com.example.mobilepowermap.Repositories;

import com.example.mobilepowermap.Models.Route;
import com.example.mobilepowermap.APIs.RouteAPI;
import com.example.mobilepowermap.Utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RouteRepository {
    private final RouteAPI apiService;

    public RouteRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance(null).create(RouteAPI.class);
    }

    public void addRoute(Route route, Callback<Route> callback) {
        Call<Route> call = apiService.addRoute(route);
        call.enqueue(callback);
    }

    public void getAllRoutes(Long userId, Callback<List<Route>> callback) {
        Call<List<Route>> call = apiService.getAllRoutes(userId);
        call.enqueue(callback);
    }

    public void addFavorite(Long routeId, Callback<Route> callback) {
        Call<Route> call = apiService.addFavorite(routeId);
        call.enqueue(callback);
    }

    public void removeFavorite(Long routeId, Callback<Route> callback) {
        Call<Route> call = apiService.removeFavorite(routeId);
        call.enqueue(callback);
    }

    public void getFavorites(Long userId, Callback<List<Route>> callback) {
        Call<List<Route>> call = apiService.getFavorites(userId);
        call.enqueue(callback);
    }
}
