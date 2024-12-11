package com.example.mobilepowermap.APIs;

import com.example.mobilepowermap.Models.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface RouteAPI {

    @POST("/api/routes")
    Call<Route> addRoute(@Body Route route);

    @GET("/api/routes/user/{userId}")
    Call<List<Route>> getAllRoutes(@Path("userId") Long userId);

    @POST("/api/routes/{routeId}/favorite")
    Call<Route> addFavorite(@Path("routeId") Long routeId);

    @DELETE("/api/routes/{routeId}/favorite")
    Call<Route> removeFavorite(@Path("routeId") Long routeId);

    @GET("/api/routes/user/{userId}/favorites")
    Call<List<Route>> getFavorites(@Path("userId") Long userId);
}

