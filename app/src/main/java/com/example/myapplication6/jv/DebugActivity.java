package com.example.myapplication6.jv;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;
import com.example.myapplication6.models.UserService;

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

        btnFirebase.setOnClickListener(v ->
                {
                    log("Sending score ✅");
                    int scoreSeconds = 100;
                    UserService.getInstance().insertScore(
                            new Score(scoreSeconds, "demo_user")
                    );


                }

        );

        btnTimer.setOnClickListener(v ->{

            GameTimer gameTimer = GameTimer.getInstance();
            if (gameTimer.isRunning()) {
                log("stop timer 🔥");

                gameTimer.stopStopwatch();
            }else{
                log("start timer 🔥");
                gameTimer.startStopwatch();
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