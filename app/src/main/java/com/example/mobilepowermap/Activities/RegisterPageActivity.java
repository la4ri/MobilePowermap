package com.example.mobilepowermap.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepowermap.Models.DTO.RegisterDTO;
import com.example.mobilepowermap.Models.User;
import com.example.mobilepowermap.R;
import com.example.mobilepowermap.Repositories.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPageActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, cpfCnpjInput;
    private RadioGroup roleGroup;
    private Button registerButton;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        nameInput = findViewById(R.id.register_name);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        cpfCnpjInput = findViewById(R.id.register_cpf_cnpj); // Certifique-se de adicionar esse campo no layout
        roleGroup = findViewById(R.id.statusRadioGroup);
        registerButton = findViewById(R.id.register_button);

        userRepository = new UserRepository();

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String cpfCnpj = cpfCnpjInput.getText().toString().trim();
        String role = ((RadioButton) findViewById(roleGroup.getCheckedRadioButtonId())).getText().toString().equals("Empresa") ? "ADMIN" : "USER";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cpfCnpj.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterDTO registerDTO = new RegisterDTO(email, password, name, cpfCnpj, role);

        userRepository.registerUser(registerDTO, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterPageActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela após o cadastro
                } else {
                    Toast.makeText(RegisterPageActivity.this, "Erro ao cadastrar usuário: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterPageActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Register", "Erro:", t);
            }
        });

    }
}
