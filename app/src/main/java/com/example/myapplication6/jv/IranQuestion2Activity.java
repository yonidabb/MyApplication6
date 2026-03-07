package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

public class IranQuestion2Activity extends AppCompatActivity {

    private TextView timerText;

    private Button ans1, ans2, ans3, ans4;

    // תשובה נכונה: "עלי חמינאי" = כפתור 2
    private static final int CORRECT = 2;

    // מגן נגד ספאם-לחיצות כפולות
    private boolean clickLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iran_question2);

        timerText = findViewById(R.id.timerText);

        ans1 = findViewById(R.id.answer1);
        ans2 = findViewById(R.id.answer2);
        ans3 = findViewById(R.id.answer3);
        ans4 = findViewById(R.id.answer4);

        bindTimer();

        ans1.setOnClickListener(v -> onAnswerChosen(1));
        ans2.setOnClickListener(v -> onAnswerChosen(2));
        ans3.setOnClickListener(v -> onAnswerChosen(3));
        ans4.setOnClickListener(v -> onAnswerChosen(4));
    }

    private void onAnswerChosen(int chosen) {
        if (clickLocked) return;

        if (chosen == CORRECT) {
            clickLocked = true;
            Toast.makeText(this, "✅ נכון! עברת שלב", Toast.LENGTH_SHORT).show();

            // מעבר מיידי לשלב הבא
            startActivity(new Intent(this, IranQuestion3Activity.class));
            finish();
        } else {
            // לא נכון -> ממשיכים לנסות עד שמצליח
            Toast.makeText(this, "❌ לא נכון, נסה שוב", Toast.LENGTH_SHORT).show();

            // אופציונלי: “אנטי-ספאם” קטן כדי שלא ילחצו 20 פעמים בשנייה
            lockClicksBriefly();
        }
    }

    private void lockClicksBriefly() {
        clickLocked = true;
        setAnswersEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setAnswersEnabled(true);
            clickLocked = false;
        }, 350);
    }

    private void setAnswersEnabled(boolean enabled) {
        ans1.setEnabled(enabled);
        ans2.setEnabled(enabled);
        ans3.setEnabled(enabled);
        ans4.setEnabled(enabled);

        float a = enabled ? 1f : 0.6f;
        ans1.setAlpha(a);
        ans2.setAlpha(a);
        ans3.setAlpha(a);
        ans4.setAlpha(a);
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
                // לא עושים כלום פה!
                // timeout מנוהל ע"י ActivityTimer (5 דקות) דרך TimerFragment
            }

        });
    }
}