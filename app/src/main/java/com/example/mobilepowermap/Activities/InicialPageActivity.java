package com.example.mobilepowermap.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepowermap.ConfigMaps.MapManager;
import com.example.mobilepowermap.R;

public class InicialPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial_page);

        // Inicializar o mapa
        MapManager.initializeMap(this, R.id.map);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Passa a resposta da permissão para o MapManager
        MapManager mapManager = new MapManager(this);
        mapManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}