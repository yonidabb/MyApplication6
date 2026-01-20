package com.example.myapplication6.jv;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        // ⏱️ לוקחים את הזמן שנשאר
        long timeLeftMillis = GameTimer.getInstance().getTimeLeftMillis();

        long seconds = timeLeftMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        // 🛑 עוצרים את הטיימר – המשחק נגמר
        GameTimer.getInstance().stop();

        // (לא חובה) הצגה על המסך אם יש TextView
        TextView resultText = findViewById(R.id.resultText);
        if (resultText != null) {
            resultText.setText(
                    String.format("⏱ הזמן שנשאר: %02d:%02d", minutes, seconds)
            );
        }

        // 🔥 כאן בהמשך:
        // TODO: שמירה ל-Firebase (Score, User, Time)
        Log.d("NextActivity", "Game finished. Time left: " + timeLeftMillis + " ms");
    }
}