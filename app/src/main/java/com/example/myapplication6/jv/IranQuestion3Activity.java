package com.example.myapplication6.jv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.GameTimer;
import com.example.myapplication6.R;

import java.util.ArrayList;
import java.util.List;

public class IranQuestion3Activity extends AppCompatActivity {

    private TextView timerText;
    private TextView feedbackText;
    private Spinner spinnerNumber, spinnerUnit;
    private Button submit;

    // התשובה הנכונה בדיוק כמו שביקשת
    private static final int CORRECT_NUMBER = 12;
    private static final String CORRECT_UNIT_EN = "days"; // יהיה "12 days"

    private static class UnitItem {
        final String labelHe; // מה שרואים בספינר
        final String valueEn; // מה שמשווים מול התשובה

        UnitItem(String labelHe, String valueEn) {
            this.labelHe = labelHe;
            this.valueEn = valueEn;
        }

        @Override
        public String toString() {
            return labelHe;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iran_question3);

        timerText = findViewById(R.id.timerText);
        feedbackText = findViewById(R.id.feedbackText);
        spinnerNumber = findViewById(R.id.spinnerNumber);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        submit = findViewById(R.id.submitButton);

        setupSpinners();
        bindTimer();

        submit.setOnClickListener(v -> checkAnswer());
    }

    private void setupSpinners() {
        // Numbers: first item is "choose"
        List<String> numbers = new ArrayList<>();
        numbers.add("בחר מספר");
        for (int i = 1; i <= 100; i++) numbers.add(String.valueOf(i));

        ArrayAdapter<String> numAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                numbers
        );
        numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumber.setAdapter(numAdapter);
        spinnerNumber.setSelection(0);

        // Units: first item is "choose"
        List<UnitItem> units = new ArrayList<>();
        units.add(new UnitItem("בחר יחידה", "")); // invalid until user chooses
        units.add(new UnitItem("ימים", "days"));
        units.add(new UnitItem("שבועות", "weeks"));
        units.add(new UnitItem("חודשים", "months"));
        units.add(new UnitItem("שנים", "years"));

        ArrayAdapter<UnitItem> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);
        spinnerUnit.setSelection(0);
    }

    private void checkAnswer() {
        int numberPos = spinnerNumber.getSelectedItemPosition();
        UnitItem selectedUnit = (UnitItem) spinnerUnit.getSelectedItem();

        // Must choose both
        if (numberPos == 0 || selectedUnit == null || selectedUnit.valueEn == null || selectedUnit.valueEn.isEmpty()) {
            feedbackText.setVisibility(View.VISIBLE);
            feedbackText.setText("בחר מספר ויחידת זמן כדי להמשיך.");
            Toast.makeText(this, "בחר ערכים לפני בדיקה", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedNumber = Integer.parseInt((String) spinnerNumber.getSelectedItem());
        String userAnswer = selectedNumber + " " + selectedUnit.valueEn;

        if (selectedNumber == CORRECT_NUMBER && CORRECT_UNIT_EN.equalsIgnoreCase(selectedUnit.valueEn)) {
            feedbackText.setVisibility(View.GONE);
            Toast.makeText(this, "✅ עברת שלב", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, IranQuestion4Activity.class));
            finish();
        } else {
            feedbackText.setVisibility(View.VISIBLE);
            feedbackText.setText("❌ לא מדויק. נסה שוב.");
            Toast.makeText(this, "❌ תשובה שגויה (" + userAnswer + ")", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // אם יש לך עוד מסכים שמקשיבים לטיימר, אפשר להשאיר.
        // אם אתה רואה התנהגות מוזרה, תגיד לי ואסדר את זה בצורה נקייה לפי הארכיטקטורה שלך.
    }
}