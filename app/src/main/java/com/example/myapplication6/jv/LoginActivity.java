package com.example.myapplication6.jv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth auth;

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private Button forgetPassButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        forgetPassButton = findViewById(R.id.forgetPassButton);
        forgetPassButton.setEnabled(false);

        // Watch for changes in email input
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                // Enable button only if valid email
//                boolean isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();

                forgetPassButton.setEnabled(RegistrationActivity.isEmail(email));

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            signIn(LoginActivity.this, email, password);
        });

        forgetPassButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            Toast.makeText(this, "will reset email to"+email, Toast.LENGTH_LONG).show();


            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            reload();
//        }
    }

    private void signIn(Context context, String email, String password) {
        Log.w(TAG, "signIn: " + email);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                        // Open AfterLoginActivity
                        context.startActivity(new Intent(context, AfterLoginActivity.class));
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Toast.makeText(this, "Authentication success.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Authentication failed or user null.", Toast.LENGTH_SHORT).show();
        }
    }

    private void reload() {
        Toast.makeText(this, "Already logged in.", Toast.LENGTH_SHORT).show();
    }
}
