package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

public class AfterLoginActivity extends AppCompatActivity {

    private TextView statusText;
    private Button startButton;
    private Button scoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        statusText = findViewById(R.id.status_text);
        startButton = findViewById(R.id.start_button);
        scoresButton = findViewById(R.id.scores_button);

        startButton.setOnClickListener(v -> startCountdown());

        scoresButton.setOnClickListener(v ->
                startActivity(new Intent(this, ScoresActivity.class))
        );
    }

    private void startCountdown() {
        statusText.setText("Starting in 5...");

        GameTimer.getInstance().startCountdown(
                5000,
                millisLeft -> runOnUiThread(() ->
                        statusText.setText(
                                "Starting in " + (millisLeft / 1000) + "..."
                        )
                ),
                () -> {
                    startActivity(new Intent(this, FirstQuestionActivity.class));
                    finish();
                }
        );
    }
}