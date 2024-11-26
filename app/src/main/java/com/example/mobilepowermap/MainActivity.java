package com.example.mobilepowermap;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepowermap.Activities.InicialPageActivity;
import com.example.mobilepowermap.ConfigMaps.MapManager;

public class MainActivity extends AppCompatActivity {
    private Button btnLoginEntrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar o mapa
        MapManager.initializeMap(this, R.id.map);

        btnLoginEntrar = findViewById(R.id.login_button);
        btnLoginEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InicialPageActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Passa a resposta da permissão para o MapManager
        MapManager mapManager = new MapManager(this);
        mapManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}



