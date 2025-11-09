package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        tvRegister.setOnClickListener(v -> {
            // Navigate to registration screen if needed
            // Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // For demo purposes, simulate successful login
        // In real app, you would validate with your backend
        if (isValidCredentials(email, password)) {
            // Save login state
            saveLoginState(email);

            // Navigate to main app
            Intent intent = new Intent(LoginActivity.this, ProductPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String email, String password) {
        // Demo validation - replace with actual authentication
        return email.equals("user@example.com") && password.equals("password");
    }

    private void saveLoginState(String email) {
        android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("user_email", email);
        editor.putString("user_name", "User"); // You'd get this from your auth system
        editor.apply();
    }
}