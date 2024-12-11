package com.example.mobilepowermap.Repositories;

import android.util.Log;

import com.example.mobilepowermap.APIs.ChargingStationAPI;
import com.example.mobilepowermap.Utils.RetrofitClient;
import com.example.mobilepowermap.Models.ChargingStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChargingStationRepository {
    private final ChargingStationAPI api;

    public ChargingStationRepository() {
        api = RetrofitClient.getRetrofitInstance(null).create(ChargingStationAPI.class);
    }

    public void getAllStations(Callback<List<ChargingStation>> callback) {
        api.getAllStations().enqueue(callback);
    }

    public void createStation(ChargingStation station, Callback<ChargingStation> callback) {
        api.createStation(station).enqueue(callback);
    }

    public void deleteStation(Long id, Callback<Void> callback) {
        api.deleteStation(id).enqueue(callback);
    }

    public void getAvailableStations(Callback<List<ChargingStation>> callback) {
        api.getAvailableStations().enqueue(callback);
    }
}
