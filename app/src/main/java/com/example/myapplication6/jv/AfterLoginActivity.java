package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AfterLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView welcomeText;
    private Button signOutButton;
    private Button profileButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        welcomeText = findViewById(R.id.welcome_text);
        signOutButton = findViewById(R.id.signout_button);
        profileButton = findViewById(R.id.profile_button);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, JMainActivity.class));
            finish();
            return;
        }

        welcomeText.setText("Hello " + currentUser.getEmail() + ", you are logged in");

        signOutButton.setOnClickListener(v -> {
            Log.d("AfterLogin", "Signing out...");
            auth.signOut();
            Log.d("AfterLogin", "Signed out");
            startActivity(new Intent(this, JMainActivity.class));
            finish();
        });

        profileButton.setOnClickListener(v -> showProfileDialog(auth.getUid()));
    }

    private void showProfileDialog(String uuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        DocumentSnapshot profile = db.collection("profiles").document(uuid).get().getResult();

        db.collection("profiles").document(uuid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        // Document exists, you can access its data
                        System.out.println("DocumentSnapshot data: " + document.getData());
                        // Example: Get the name field
                        String name = document.getString("name");
                        System.out.println("City Name: " + name);
                        builder.setTitle("User Profile");
                        builder.setMessage("Name: "+document.get("name")+","+document.get("age"));
                        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
                        builder.show();

                    } else {
                        // Document doesn't exist
                        System.out.println("No such document");
                    }
                })
                .addOnFailureListener(exception -> {
                    // Oh no! Handle the error
                    System.err.println("Error getting documents: " + exception);
                });


    }
}
