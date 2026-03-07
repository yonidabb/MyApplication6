package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IranQuestion5Activity extends AppCompatActivity {

    private TextView timerText;
    private EditText answer;

    private static final String CORRECT = "haman"; // התשובה הנכונה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iran_question5);

        timerText = findViewById(R.id.timerText);
        answer = findViewById(R.id.answerInput);
        Button submit = findViewById(R.id.submitButton);

        bindTimer();

        submit.setOnClickListener(v -> {
            String userAns = answer.getText().toString().trim();

            if (userAns.equalsIgnoreCase(CORRECT)) {
                win();
            } else {
                Toast.makeText(this, "❌ תשובה שגויה — בוא ננסה בחירה", Toast.LENGTH_SHORT).show();
                showChoicesUntilCorrect();
            }
        });
    }

    private void showChoicesUntilCorrect() {
        List<String> options = new ArrayList<>();
        options.add(CORRECT);
        options.add("xerxes");
        options.add("cyrus");

        // אם בטעות הנכון שווה לאחד הפיתיונות אחרי שינוי עתידי — מסדרים
        options = uniqueIgnoreCase(options);
        while (options.size() < 3) options.add("darius");

        Collections.shuffle(options);

        String[] items = options.toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("בחר את התשובה הנכונה")
                .setCancelable(false)
                .setItems(items, (dialog, which) -> {
                    String picked = items[which];

                    if (picked.equalsIgnoreCase(CORRECT)) {
                        win();
                    } else {
                        Toast.makeText(this, "❌ לא, נסה שוב", Toast.LENGTH_SHORT).show();
                        showChoicesUntilCorrect();
                    }
                })
                .show();
    }

    private List<String> uniqueIgnoreCase(List<String> list) {
        List<String> out = new ArrayList<>();
        for (String s : list) {
            boolean exists = false;
            for (String t : out) {
                if (t.equalsIgnoreCase(s)) { exists = true; break; }
            }
            if (!exists) out.add(s);
        }
        return out;
    }

    private void win() {
        Toast.makeText(this, "🏁 סיימת את המשחק!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, NextActivity.class));
        finish();
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