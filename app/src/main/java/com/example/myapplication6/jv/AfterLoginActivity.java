package com.example.myapplication6.jv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;
import com.example.myapplication6.models.Match;
import com.example.myapplication6.models.UserService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AfterLoginActivity extends AppCompatActivity {

    private static final String PREFS = "app_prefs";
    private static final String KEY_USERNAME = "username";

    private TextView statusText;
    private TextView helloText;
    private Button startButton;
    private Button scoresButton;
    private Button aiButton;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        statusText = findViewById(R.id.status_text);
        startButton = findViewById(R.id.start_button);
        scoresButton = findViewById(R.id.scores_button);
        aiButton = findViewById(R.id.aiExam_button);
        helloText = findViewById(R.id.helloUser);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // כפתור ☰ לפתיחת ה-side menu
        ImageButton btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // הכפתורים הקיימים שלך
        startButton.setOnClickListener(v -> startCountdown());
        scoresButton.setOnClickListener(v -> startActivity(new Intent(this, ScoresActivity.class)));
        aiButton.setOnClickListener(v -> startActivity(new Intent(this, ExamActivity.class)));

        // ה-side menu
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_start) {
                startCountdown();
            } else if (id == R.id.nav_scores) {
                startActivity(new Intent(this, ScoresActivity.class));
            } else if (id == R.id.nav_ai) {
                startActivity(new Intent(this, ExamActivity.class));
            } else if (id == R.id.nav_logout) {
                doLogout();
            } else if (id == R.id.nav_exit) {
                finishAffinity(); // יציאה מהמשחק
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // בדיקת משתמש
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Log.d("AfterLoginActivity", "displayName=" + user.getDisplayName());
        Log.d("AfterLoginActivity", "email=" + user.getEmail());

        String name = user.getDisplayName();
        if (name == null || name.trim().isEmpty()) {
            name = usernameFromEmail(user.getEmail());
        }

        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        sp.edit().putString(KEY_USERNAME, name).apply();

        helloText.setText("Hello " + name);
    }

    private String usernameFromEmail(String email) {
        if (email == null) return "Agent";
        int at = email.indexOf('@');
        if (at <= 0) return email;
        return email.substring(0, at);
    }

    private void startCountdown() {
        statusText.setText("Starting in 5...");

        GameTimer.getInstance().startCountdown(
                5000,
                millisLeft -> runOnUiThread(() ->
                        statusText.setText("Starting in " + (millisLeft / 1000) + "...")
                ),
                () -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Log.e("SCORE_SAVE", "No user logged in - not saving score");
                        return;
                    }

                    String userKey = user.getUid();
                    UserService.getInstance().insertNewMatch(new Match(user));
//                    userService.insertScore(new Score((int) score, user));
                    startActivity(new Intent(this, FirstQuestionActivity.class));
                    finish();
                }
        );
    }

    private void doLogout() {
        FirebaseAuth.getInstance().signOut();

        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        sp.edit().remove(KEY_USERNAME).apply();

        Intent i = new Intent(this, JMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}