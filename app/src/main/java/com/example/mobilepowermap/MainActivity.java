package com.example.mobilepowermap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepowermap.Activities.InicialPageActivity;
import com.example.mobilepowermap.Activities.RegisterPageActivity;
import com.example.mobilepowermap.Models.DTO.AuthenticationDTO;
import com.example.mobilepowermap.Models.DTO.LoginResponseDTO;
import com.example.mobilepowermap.Utils.AuthService;
import com.example.mobilepowermap.Utils.Constants;
import com.example.mobilepowermap.Utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnLoginEntrar;
    private EditText emailLogin, passwordLogin;
    private TextView cadastrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular os campos de entrada e botão
        emailLogin = findViewById(R.id.email);
        passwordLogin = findViewById(R.id.password);
        btnLoginEntrar = findViewById(R.id.login_button);
        cadastrarUsuario = findViewById(R.id.register);

        // Configurar o evento de clique do botão de login
        btnLoginEntrar.setOnClickListener(v -> login());

        // Configurar o evento de clique no botão de cadastro
        cadastrarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterPageActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthenticationDTO authData = new AuthenticationDTO(email, password);

        // Chamar o serviço de autenticação usando Retrofit
        AuthService authService = RetrofitClient.getRetrofitInstance(null).create(AuthService.class);
        authService.login(authData).enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Armazenar o token em SharedPreferences
                    String token = response.body().getToken();
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
                    sharedPreferences.edit().putString(Constants.TOKEN_KEY, token).apply();

                    // Redirecionar para a tela inicial
                    Intent intent = new Intent(MainActivity.this, InicialPageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                Log.e("MainActivity", "Erro de conexão", t);
                Toast.makeText(MainActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}