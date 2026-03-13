package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;

import com.google.firebase.messaging.FirebaseMessaging;


public class JMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
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

    public void init() {
        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed to all_users topic!";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    Log.d("FCM", msg);
                });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = "FCM Registration Token: " + token;
                    Log.d("FCM", msg);
                });
    }
}