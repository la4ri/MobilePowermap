package com.example.mobilepowermap.ConfigMaps;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mobilepowermap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.pm.PackageManager;
import android.Manifest;

public class MapManager implements OnMapReadyCallback {

    private static final String TAG = "MapManager";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private FragmentActivity activity;

    public MapManager(FragmentActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static void initializeMap(FragmentActivity activity, int mapFragmentId) {
        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
                .findFragmentById(mapFragmentId);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new MapManager(activity));
        } else {
            Log.e(TAG, "Erro: fragmento do mapa não encontrado.");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        // Verificar permissões de localização
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permissões já concedidas, inicializar o mapa
            mMap.setMyLocationEnabled(true);
            requestUserLocation();
        } else {
            // Solicitar permissões
            requestLocationPermissions();
        }

        // Configurar o estilo do mapa
        applyMapStyle();
    }

    private void requestLocationPermissions() {
        // Solicitar permissões de localização
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void requestUserLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Obter coordenadas da localização
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            // Atualizar a câmera para a localização do usuário
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                            // Adicionar marcador para a localização do usuário
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("Você está aqui"));
                        }
                    }
                });
    }

    private void applyMapStyle() {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style));
            if (!success) {
                Log.e(TAG, "Falha ao carregar o estilo do mapa.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar o estilo do mapa.", e);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, inicializar a localização do usuário
                mMap.setMyLocationEnabled(true);
                requestUserLocation();
            } else {
                // Permissão negada, tratar de acordo
                Log.e(TAG, "Permissão de localização negada.");
            }
        }
    }
}

