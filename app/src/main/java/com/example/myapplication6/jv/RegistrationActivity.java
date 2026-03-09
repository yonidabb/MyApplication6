package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.example.myapplication6.models.CountryItem;
import com.example.myapplication6.models.UserProfile;
import com.example.myapplication6.models.UserService;

import java.util.ArrayList;
import java.util.List;

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

        setupCountrySpinner();

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void setupCountrySpinner() {
        List<CountryItem> countries = new ArrayList<>();
        countries.add(new CountryItem("Israel", "🇮🇱"));
        countries.add(new CountryItem("United States", "🇺🇸"));
        countries.add(new CountryItem("United Kingdom", "🇬🇧"));
        countries.add(new CountryItem("France", "🇫🇷"));
        countries.add(new CountryItem("Germany", "🇩🇪"));
        countries.add(new CountryItem("Italy", "🇮🇹"));
        countries.add(new CountryItem("Spain", "🇪🇸"));
        countries.add(new CountryItem("Portugal", "🇵🇹"));
        countries.add(new CountryItem("Greece", "🇬🇷"));
        countries.add(new CountryItem("Cyprus", "🇨🇾"));
        countries.add(new CountryItem("Canada", "🇨🇦"));
        countries.add(new CountryItem("Australia", "🇦🇺"));
        countries.add(new CountryItem("Brazil", "🇧🇷"));
        countries.add(new CountryItem("Argentina", "🇦🇷"));
        countries.add(new CountryItem("Mexico", "🇲🇽"));
        countries.add(new CountryItem("India", "🇮🇳"));
        countries.add(new CountryItem("China", "🇨🇳"));
        countries.add(new CountryItem("Japan", "🇯🇵"));
        countries.add(new CountryItem("South Korea", "🇰🇷"));
        countries.add(new CountryItem("Thailand", "🇹🇭"));
        countries.add(new CountryItem("South Africa", "🇿🇦"));
        countries.add(new CountryItem("Egypt", "🇪🇬"));
        countries.add(new CountryItem("Morocco", "🇲🇦"));
        countries.add(new CountryItem("Turkey", "🇹🇷"));
        countries.add(new CountryItem("Russia", "🇷🇺"));
        countries.add(new CountryItem("Ukraine", "🇺🇦"));
        countries.add(new CountryItem("Sweden", "🇸🇪"));
        countries.add(new CountryItem("Norway", "🇳🇴"));
        countries.add(new CountryItem("Denmark", "🇩🇰"));
        countries.add(new CountryItem("Finland", "🇫🇮"));
        countries.add(new CountryItem("Poland", "🇵🇱"));
        countries.add(new CountryItem("Netherlands", "🇳🇱"));
        countries.add(new CountryItem("Belgium", "🇧🇪"));
        countries.add(new CountryItem("Switzerland", "🇨🇭"));
        countries.add(new CountryItem("Austria", "🇦🇹"));
        countries.add(new CountryItem("Hungary", "🇭🇺"));
        countries.add(new CountryItem("Czech Republic", "🇨🇿"));
        countries.add(new CountryItem("Romania", "🇷🇴"));
        countries.add(new CountryItem("Bulgaria", "🇧🇬"));
        countries.add(new CountryItem("Croatia", "🇭🇷"));
        countries.add(new CountryItem("Serbia", "🇷🇸"));
        countries.add(new CountryItem("Slovenia", "🇸🇮"));
        countries.add(new CountryItem("Singapore", "🇸🇬"));
        countries.add(new CountryItem("Philippines", "🇵🇭"));
        countries.add(new CountryItem("Vietnam", "🇻🇳"));
        countries.add(new CountryItem("Indonesia", "🇮🇩"));
        countries.add(new CountryItem("Malaysia", "🇲🇾"));
        countries.add(new CountryItem("New Zealand", "🇳🇿"));
        countries.add(new CountryItem("United Arab Emirates", "🇦🇪"));
        countries.add(new CountryItem("Qatar", "🇶🇦"));
        countries.add(new CountryItem("Saudi Arabia", "🇸🇦"));

        CountryAdapter adapter = new CountryAdapter(this, countries);
        stateSpinner.setAdapter(adapter);
    }

    private void attemptRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        CountryItem selectedCountry = (CountryItem) stateSpinner.getSelectedItem();
        String state = selectedCountry != null ? selectedCountry.getName() : "";
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