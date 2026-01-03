package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.example.myapplication6.models.UserProfile;
import com.example.myapplication6.models.UserService;

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, ageInput, passwordInput, confirmPasswordInput;
    private Spinner stateSpinner;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        ageInput = findViewById(R.id.ageInput);
        stateSpinner = findViewById(R.id.stateSpinner);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.countries_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        String state = stateSpinner.getSelectedItem().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (name.isEmpty() || email.isEmpty() || ageStr.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidName(name)) {
            Toast.makeText(this,
                    "Name can contain only English letters and spaces",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String emailError = checkEmailError(email);
        if (emailError != null) {
            Toast.makeText(this, emailError, Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile profile = new UserProfile(
                name,
                email,
                ageStr,
                state,
                password
        );

        // ✅ שימוש נכון ב־UserService החדש
        UserService.getInstance().registerUser(
                this,
                profile,
                () -> {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, AfterLoginActivity.class));
                    finish();
                }
        );
    }

    // ===== Validation =====
    private boolean isValidName(String name) {
        if (name == null || name.length() < 2) return false;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c) && c != ' ') return false;
        }

        return !(name.startsWith(" ") || name.endsWith(" "));
    }

    private String checkEmailError(String email) {
        if (email == null || email.isEmpty()) {
            return "Please enter an email address";
        }

        if (email.contains(" ")) {
            return "Email cannot contain spaces";
        }

        int atIndex = email.indexOf('@');
        int lastAtIndex = email.lastIndexOf('@');

        if (atIndex <= 0) return "Email must contain a username before '@'";
        if (atIndex != lastAtIndex) return "Email can contain only one '@'";
        if (atIndex == email.length() - 1) return "Email must contain a domain after '@'";

        String domainPart = email.substring(atIndex + 1);
        int dotIndex = domainPart.indexOf('.');

        if (dotIndex <= 0) return "Domain must contain at least one '.'";
        if (dotIndex == domainPart.length() - 1) return "Domain cannot end with '.'";

        return null;
    }
}