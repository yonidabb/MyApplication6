package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;
import com.example.myapplication6.models.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class NextActivity extends AppCompatActivity {

    private ImageView bgImage;
    private TextView resultText, logText, titleText;
    private Button backButton;

    private Thread crashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        bgImage = findViewById(R.id.bgImage);
        resultText = findViewById(R.id.resultText);
        logText = findViewById(R.id.logText);
        titleText = findViewById(R.id.titleText);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(NextActivity.this, AfterLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        long timeLeftMillis = GameTimer.getInstance().getTimeLeftMillis();

        long seconds = timeLeftMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        GameTimer.getInstance().stop();

        resultText.setText(String.format("⏱ הזמן שנשאר: %02d:%02d", minutes, seconds));
        Log.d("NextActivity", "Game finished. Time left: " + timeLeftMillis + " ms");

        if (savedInstanceState == null) {
            saveScoreToFirebase(timeLeftMillis);
        }

        startCrashThread();
    }

    private void saveScoreToFirebase(long timeLeftMillis) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("SCORE_SAVE", "No user logged in - not saving score");
            return;
        }

        String userKey = user.getUid();
        int scoreSec = (int) (timeLeftMillis / 1000);

        if (scoreSec <= 0) {
            Log.w("SCORE_SAVE", "Ignoring scoreSec<=0. scoreSec=" + scoreSec);
            return;
        }

        UserService.getInstance().insertScore(new Score(scoreSec, userKey));
        Log.d("SCORE_SAVE", "Saved scoreSec=" + scoreSec + " user=" + userKey);
    }

    private void startCrashThread() {
        crashThread = new Thread(() -> {
            Random rnd = new Random();

            String[] lines = new String[]{
                    "> Reactor status: STABLE",
                    "> Cooling system: OK",
                    "> Running shutdown protocol...",
                    "> Warning: pressure rising",
                    "> ALERT: anomaly detected",
                    "> WARNING: coolant instability",
                    "> ERROR: stabilization failed",
                    "> SYSTEM CRASH INITIATED",
                    "> CORE BREACH IMMINENT",
                    "> REACTOR OFFLINE"
            };

            final int[] lastStage = {-1};

            for (int p = 0; p <= 100; p += 2) {

                if (Thread.currentThread().isInterrupted()) return;

                int stage;
                if (p < 40) stage = 0;
                else if (p < 80) stage = 1;
                else stage = 2;

                String line = lines[Math.min(lines.length - 1, p / 10)];

                int finalStage = stage;
                String finalLine = line;

                runOnUiThread(() -> {
                    logText.setText(finalLine);

                    if (finalStage != lastStage[0]) {
                        if (finalStage == 0) {
                            bgImage.setImageResource(R.drawable.reactor_stable);
                            titleText.setText("✅ סיום משימה");
                        } else if (finalStage == 1) {
                            bgImage.setImageResource(R.drawable.reactor_warning);
                            titleText.setText("⚠️ מערכת לא יציבה");
                            glitch(bgImage);
                            blink(titleText);
                        } else {
                            bgImage.setImageResource(R.drawable.reactor_crash);
                            titleText.setText("💥 SYSTEM CRASH");
                            glitch(bgImage);
                            blink(titleText);
                            blink(logText);
                        }
                        lastStage[0] = finalStage;
                    }
                });

                try {
                    Thread.sleep(60 + rnd.nextInt(90));
                } catch (InterruptedException e) {
                    return;
                }
            }

            runOnUiThread(() -> {
                logText.setText("> Shutdown complete. Reactor offline.");

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        backButton.setVisibility(View.VISIBLE);
                    }
                }, 3000);
            });
        });

        crashThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (crashThread != null) crashThread.interrupt();
    }

    private void glitch(View v) {
        TranslateAnimation shake = new TranslateAnimation(-10, 10, 0, 0);
        shake.setDuration(80);
        shake.setRepeatCount(2);
        shake.setRepeatMode(Animation.REVERSE);
        v.startAnimation(shake);
    }

    private void blink(View v) {
        v.setAlpha(v.getAlpha() == 1f ? 0.35f : 1f);
    }
}