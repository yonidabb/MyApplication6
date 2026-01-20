package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

public class IranQuestion5Activity extends AppCompatActivity {

    private TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iran_question5);

        timerText = findViewById(R.id.timerText);
        EditText answer = findViewById(R.id.answerInput);
        Button submit = findViewById(R.id.submitButton);

        bindTimer();

        submit.setOnClickListener(v -> {
            if (answer.getText().toString().trim().equalsIgnoreCase("haman")) {
                Toast.makeText(this, "🏁 סיימת את המשחק!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, NextActivity.class));
                finish();
            } else {
                Toast.makeText(this, "❌ תשובה שגויה", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindTimer() {
        GameTimer.getInstance().setListener(new GameTimer.TimerListener() {
            @Override
            public void onTick(long ms) {
                long s = ms / 1000;
                runOnUiThread(() ->
                        timerText.setText(String.format("⏱ %02d:%02d", s / 60, s % 60))
                );
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(IranQuestion5Activity.this, NextActivity.class));
                finish();
            }
        });
    }
}