package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;

public class JMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jmain);

        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(v ->
                startActivity(new Intent(JMainActivity.this, LoginActivity.class))
        );

        createAccountButton.setOnClickListener(v ->
                startActivity(new Intent(JMainActivity.this, RegistrationActivity.class))
        );
    }
}