package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

public class FirstQuestionActivity extends AppCompatActivity {

    // ===== Views =====
    private TextView codeScreen, puzzleButton;
    private TextView num0, num1, num2, num3, num4, num5, num6, num7, num8, num9, deleteBtn;

    // ===== Logic =====
    private final StringBuilder enteredCode = new StringBuilder();
    private int currentPuzzleIndex = 0;

    // ===== Puzzle Model =====
    private static class Puzzle {
        String answer;
        String question;

        Puzzle(String answer, String question) {
            this.answer = answer;
            this.question = question;
        }
    }

    // ===== All Puzzles =====
    private final Puzzle[] puzzles = {
            new Puzzle("1948", "באיזו שנה הוקמה מדינת ישראל?"),
            new Puzzle("1969", "באיזו שנה נחת האדם הראשון על הירח?"),
            new Puzzle("1876", "באיזו שנה הומצא הטלפון?"),
            new Puzzle("1989", "באיזו שנה נפלה חומת ברלין?")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_question);

        // ⏱️ מתחילים טיימר פעם אחת בלבד (ה-Fragment מציג אותו)
        if (!GameTimer.getInstance().isRunning()) {
            GameTimer.getInstance().start(5 * 60 * 1000); // 5 דקות
        }

        initViews();
        setupNumberPad();

        puzzleButton.setOnClickListener(v -> showCurrentQuestion());
    }

    // ===== Init =====
    private void initViews() {
        codeScreen = findViewById(R.id.codeScreen);
        puzzleButton = findViewById(R.id.puzzleButton);

        num0 = findViewById(R.id.num0);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);
        deleteBtn = findViewById(R.id.deleteBtn);
    }

    private void setupNumberPad() {
        num0.setOnClickListener(v -> addDigit("0"));
        num1.setOnClickListener(v -> addDigit("1"));
        num2.setOnClickListener(v -> addDigit("2"));
        num3.setOnClickListener(v -> addDigit("3"));
        num4.setOnClickListener(v -> addDigit("4"));
        num5.setOnClickListener(v -> addDigit("5"));
        num6.setOnClickListener(v -> addDigit("6"));
        num7.setOnClickListener(v -> addDigit("7"));
        num8.setOnClickListener(v -> addDigit("8"));
        num9.setOnClickListener(v -> addDigit("9"));
        deleteBtn.setOnClickListener(v -> deleteLastDigit());
    }

    // ===== Input =====
    private void addDigit(String digit) {
        if (enteredCode.length() >= puzzles[currentPuzzleIndex].answer.length()) return;

        enteredCode.append(digit);
        codeScreen.setText(enteredCode.toString());

        if (enteredCode.length() == puzzles[currentPuzzleIndex].answer.length()) {
            checkAnswer();
        }
    }

    private void deleteLastDigit() {
        if (enteredCode.length() == 0) return;
        enteredCode.deleteCharAt(enteredCode.length() - 1);
        codeScreen.setText(enteredCode.toString());
    }

    // ===== Game Logic =====
    private void checkAnswer() {
        Puzzle puzzle = puzzles[currentPuzzleIndex];

        if (enteredCode.toString().equals(puzzle.answer)) {
            Toast.makeText(this, "✔ נכון!", Toast.LENGTH_SHORT).show();
            moveToNextPuzzle();
        } else {
            Toast.makeText(this, "✖ תשובה שגויה", Toast.LENGTH_SHORT).show();
            resetInput();
        }
    }

    private void moveToNextPuzzle() {
        resetInput();
        currentPuzzleIndex++;

        if (currentPuzzleIndex >= puzzles.length) {
            Toast.makeText(this, "🎉 סיימת שלב!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, IranPuzzleActivity.class));
            finish();
        }
    }

    private void resetInput() {
        enteredCode.setLength(0);
        codeScreen.setText("");
    }

    private void showCurrentQuestion() {
        Toast.makeText(this, puzzles[currentPuzzleIndex].question, Toast.LENGTH_LONG).show();
    }
}