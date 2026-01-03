package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.example.myapplication6.models.UserService;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, forgetPassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        forgetPassButton = findViewById(R.id.forgetPassButton);

        // Disabled until valid email
        forgetPassButton.setEnabled(false);

        emailInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                forgetPassButton.setEnabled(
                        Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()
                );
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        loginButton.setOnClickListener(v -> attemptLogin());

        forgetPassButton.setOnClickListener(v -> resetPassword());
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserService.getInstance().signIn(
                this,
                email,
                password,
                () -> {
                    // ✅ מעבר למסך הבא קורה כאן
                    startActivity(new Intent(this, AfterLoginActivity.class));
                    finish();
                }
        );
    }

    private void resetPassword() {
        String email = emailInput.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email to reset password", Toast.LENGTH_SHORT).show();
            return;
        }

        UserService.getInstance().sendPasswordResetEmail(this, email);
    }
}