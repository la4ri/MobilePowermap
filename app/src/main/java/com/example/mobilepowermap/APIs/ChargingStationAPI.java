package com.example.mobilepowermap.APIs;

import com.example.mobilepowermap.Models.ChargingStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ChargingStationAPI {
    @GET("/api/stations")
    Call<List<ChargingStation>> getAllStations();

    @POST("/api/stations")
    Call<ChargingStation> createStation(@Body ChargingStation station);

    @DELETE("/api/stations/{id}")
    Call<Void> deleteStation(@Path("id") Long id);

    @GET("/api/stations/available")
    Call<List<ChargingStation>> getAvailableStations();
}
