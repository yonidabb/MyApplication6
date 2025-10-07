package com.example.myapplication6.jv;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.example.myapplication6.db.DBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";

    private EditText nameInput, emailInput, ageInput, stateInput, passwordInput;
    private Button registerButton;
    private DBManager dbManager;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbManager = new DBManager(this);
        auth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        ageInput = findViewById(R.id.ageInput);
        stateInput = findViewById(R.id.stateInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String ageStr = ageInput.getText().toString().trim();
            String state = stateInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            if (name.isEmpty() || email.isEmpty() || ageStr.isEmpty() || state.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isEmail(email)){
                Toast.makeText(RegistrationActivity.this, "please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cheakPassword(password)){
                Toast.makeText(RegistrationActivity.this, "The password is too short", Toast.LENGTH_SHORT).show();
                return;
            }



            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age>120){
                    Toast.makeText(RegistrationActivity.this, "Age is not in rage", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(RegistrationActivity.this, "Invalid age", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(email, password,name,ageStr,state);
//            registerUserProfile(name, email,ageStr,state);

            // Save user in DBManager (local database)
//            dbManager.open();
//            dbManager.insert(name, email);
//            dbManager.close();

        });
    }

    //yonidab@gmail.com
    //yoni.tammam@sap.com
    //yoni.tammam@sap
    public static boolean isEmail(String email) {
        if (email == null || email.isEmpty()) return false;

        int atIndex = email.indexOf('@');
        int lastAtIndex = email.lastIndexOf('@');

        // Must contain exactly one '@'
        if (atIndex <= 0 || atIndex != lastAtIndex || atIndex == email.length() - 1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);

        // Domain must contain at least one '.' and must not start or end with '.'
        int dotIndex = domainPart.indexOf('.');
        if (dotIndex <= 0 || dotIndex == domainPart.length() - 1) {
            return false;
        }

        // Optional: check for spaces (not allowed in valid email)
        if (email.contains(" ")) {
            return false;
        }

        return true;
    }

    private boolean cheakPassword(String password) {

        return password.length()>=6;
    }

    private void registerUserProfile(String name, String email, String ageStr, String state) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> session = new HashMap<>();
        session.put("name", name);
        session.put("email", email);
        session.put("age", ageStr);
        session.put("state", state);
        session.put("current", LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        db.collection("profiles")
                .document(auth.getUid())
                .set(session)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: " ))
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error adding document", e));
    }

    private void registerUserProfile(String uuid,String name, String email, String ageStr, String state) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> profile = new HashMap<>();
        profile.put("name", name);
        profile.put("email", email);
        profile.put("age", ageStr);
        profile.put("state", state);
        profile.put("current", LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        db.collection("profiles")
                .document(uuid)
                .set(profile)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: " + uuid))
                .addOnFailureListener(e ->
                        Log.w(TAG, "Error adding document", e));
    }

    private void registerUser(String email, String password, String name, String ageStr, String state) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            registerUserProfile(user.getUid(),name,email,ageStr,state);
                            Toast.makeText(RegistrationActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();


//                        updateUI(user);
                        } else {
                            // If sign in fails
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "User Registered failed", Toast.LENGTH_SHORT).show();

//                        updateUI(null);
                        }
                    }
                });
    }
}
