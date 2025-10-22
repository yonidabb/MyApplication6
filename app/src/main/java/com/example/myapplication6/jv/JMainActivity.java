package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JMainActivity extends AppCompatActivity {

    private final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jmain);
        setupFirestore();

        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(v -> {
            Log.d("open", "open login1");
            startActivity(new Intent(JMainActivity.this, LoginActivity.class));
        });

        createAccountButton.setOnClickListener(v -> {
            Log.d("open", "open sign up");
            startActivity(new Intent(JMainActivity.this, RegistrationActivity.class));
        });
    }

    private void setupFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> session = new HashMap<>();
        session.put("first", "Ada");
        session.put("last", "Lovelace");
        session.put("born", 1815);
        session.put("current", LocalDateTime.now().toLocalDate().toString());

        db.collection("sessions1")
                .add(session)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error adding document", e));
    }
}
