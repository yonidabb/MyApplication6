package com.example.myapplication6.jv;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.ActivityTimer;
import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;
import com.example.myapplication6.models.UserService;

import java.util.Random;

public class DebugActivity extends AppCompatActivity {

    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        resultText = findViewById(R.id.debugResultText);

        Button btnTimer = findViewById(R.id.btnTestTimer);
        Button btnFirebase = findViewById(R.id.btnTestFirebase);
        Button btnSound = findViewById(R.id.btnTestSound);
        Button btnClear = findViewById(R.id.btnClear);

        ActivityTimer activityTimer = ActivityTimer.getInstance();


        btnFirebase.setOnClickListener(v ->
                {
                    // Random number between 50 and 400
                    long timeLeftMillis = new Random().nextInt(400 - 50 + 1) + 50;
                    timeLeftMillis*=1000;

                    log("Sending score ✅ (" + timeLeftMillis + "ms)");
                    UserService.getInstance().saveScoreToFirebase(timeLeftMillis);



                }

        );

        btnTimer.setOnClickListener(v -> {

                    if (activityTimer.isRunning()) {
                        log("timer time is " + activityTimer.getGameTime());

                    } else {
                        log("start timer 🔥");
                        activityTimer.startTimer();
                    }


                }
        );

        btnSound.setOnClickListener(v ->
                log("Sound test clicked 🔊"));

        btnClear.setOnClickListener(v ->
                resultText.setText("Debug output cleared"));
    }

    private void log(String message) {
        String current = resultText.getText().toString();
        resultText.setText(current + "\n" + message);
    }
}