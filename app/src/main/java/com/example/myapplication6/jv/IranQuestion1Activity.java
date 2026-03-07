package com.example.myapplication6.jv;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

import java.util.Locale;

public class IranQuestion1Activity extends AppCompatActivity {

    private TextView timerText;
    private EditText answerInput;
    private Button submitButton;

    private static final int CORRECT_YEAR = 1979;

    private final Runnable timerUiUpdater = new Runnable() {
        @Override
        public void run() {
            long ms = GameTimer.getInstance().getTimeLeftMillis();
            timerText.setText("⏱ " + formatTime(ms));
            timerText.postDelayed(this, 250);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iran_question1);

        timerText = findViewById(R.id.timerText);
        answerInput = findViewById(R.id.answerInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> checkAnswer());
    }

    @Override
    protected void onStart() {
        super.onStart();
        timerText.removeCallbacks(timerUiUpdater);
        timerText.post(timerUiUpdater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerText.removeCallbacks(timerUiUpdater);
    }

    private void checkAnswer() {
        String s = (answerInput.getText() == null) ? "" : answerInput.getText().toString().trim();

        if (TextUtils.isEmpty(s)) {
            Toast.makeText(this, "תכתוב שנה", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "רק מספרים (4 ספרות)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (year == CORRECT_YEAR) {
            Toast.makeText(this, "✅ נכון!", Toast.LENGTH_SHORT).show();

            // TODO: תחליף ל-Activity האמיתי של שלב 2 אצלך
            // startActivity(new Intent(this, IranQuestion2Activity.class));

            startActivity(new Intent(IranQuestion1Activity.this, IranQuestion2Activity.class));
            finish();
        } else {
            Toast.makeText(this, "❌ לא נכון, נסה שוב", Toast.LENGTH_SHORT).show();
            answerInput.setText("");
            answerInput.requestFocus();
        }
    }

    private static String formatTime(long millis) {
        if (millis < 0) millis = 0;
        long totalSeconds = millis / 1000;
        long min = totalSeconds / 60;
        long sec = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", min, sec);
    }
}