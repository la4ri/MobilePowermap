package com.example.mobilepowermap.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobilepowermap.MainActivity;
import com.example.mobilepowermap.R;
import com.example.mobilepowermap.Models.ChargingStation;
import com.example.mobilepowermap.Repositories.ChargingStationRepository;
import com.example.mobilepowermap.Utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicialPageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "InicialPageActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private TextView messageText;
    private Button startButton;
    private ImageView logoutButton, searchButton;
    private FusedLocationProviderClient fusedLocationClient;
    private ChargingStationRepository repository;

    private HashMap<Marker, ChargingStation> markerStationMap = new HashMap<>();
    private Marker lastSelectedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial_page);

        repository = new ChargingStationRepository();


        messageText = findViewById(R.id.message_text);
        startButton = findViewById(R.id.start_button);
        logoutButton = findViewById(R.id.button_logout);
//        searchButton = findViewById(R.id.search_button);


        messageText.setText("Selecione um terminal");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeMap();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String role = sharedPreferences.getString(Constants.ROLE_KEY, "USER");

        if (role != null) {
            if (role.equals("ADMIN")) {
                renderAdminView();
            } else if (role.equals("USER")) {
                renderUserView();
            }
        } else {
            Toast.makeText(this, "Role não encontrada. Faça login novamente.", Toast.LENGTH_SHORT).show();
            finish();
        }


        startButton.setOnClickListener(v ->
                Toast.makeText(this, "Selecione uma estação antes de iniciar", Toast.LENGTH_SHORT).show()
        );


        logoutButton.setOnClickListener(v -> {
            // Voltar para a MainActivity
            Intent intent = new Intent(InicialPageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

//        searchButton.setOnClickListener(v -> {
//            Intent intent = new Intent(InicialPageActivity.this, SearchActivity.class);
//            startActivity(intent);
//            finish();
//        });
    }

    private void renderAdminView() {
        findViewById(R.id.admin_view).setVisibility(View.VISIBLE);
        findViewById(R.id.user_view).setVisibility(View.GONE);
    }

    private void renderUserView() {
        findViewById(R.id.user_view).setVisibility(View.VISIBLE);
        findViewById(R.id.admin_view).setVisibility(View.GONE);
    }

    private void initializeMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Erro: fragmento do mapa não encontrado.");
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success) {
                Log.e(TAG, "Erro ao aplicar o estilo do mapa.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar o estilo do mapa: " + e.getMessage());
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
            });
        }

        fetchMarkersFromApi();

        mMap.setOnMarkerClickListener(marker -> {
            if (lastSelectedMarker != null) {
                lastSelectedMarker.setIcon(getMarkerDefaultIcon());
            }

            marker.setIcon(getMarkerClickIcon());
            lastSelectedMarker = marker;

            ChargingStation station = markerStationMap.get(marker);
            if (station != null) {
                messageText.setText(getStationMessage(station));
                startButton.setOnClickListener(v ->
                        Toast.makeText(this, "Iniciando rota para " + station.getName(), Toast.LENGTH_SHORT).show()
                );
            }

            return false;
        });
    }

    private void fetchMarkersFromApi() {
        repository.getAllStations(new Callback<List<ChargingStation>>() {
            @Override
            public void onResponse(Call<List<ChargingStation>> call, Response<List<ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChargingStation> stations = response.body();

                    for (ChargingStation station : stations) {
                        LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
                        runOnUiThread(() -> {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(station.getName())
                                    .icon(getMarkerDefaultIcon()));
                            if (marker != null) {
                                markerStationMap.put(marker, station);
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Erro ao buscar marcadores: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ChargingStation>> call, Throwable t) {
                Log.e(TAG, "Falha na API: " + t.getMessage());
            }
        });
    }

    private String getStationMessage(ChargingStation station) {
        String name = station.getName();
        int capacity = station.getCapacity();
        boolean available = station.getAvailable();

        if (!available) {
            return "Estação indisponível no momento";
        }

        if (capacity < 30) {
            return "Estação " + name + ": rota com alto tempo de espera e carga em -30%";
        } else if (capacity >= 40 && capacity <= 70) {
            return "Estação " + name + ": rota com menos tempo de espera e carga em " + capacity + "%";
        } else if (capacity >= 80) {
            return "Estação " + name + ": Melhor rota com menos tempo de espera e carga em " + capacity + "%";
        }

        return "Estação " + name + ": capacidade em " + capacity + "%";
    }

    private BitmapDescriptor getMarkerDefaultIcon() {
        return BitmapDescriptorFactory.fromResource(R.raw.station);
    }

    private BitmapDescriptor getMarkerClickIcon() {
        return BitmapDescriptorFactory.fromResource(R.raw.station_click);
    }
}
