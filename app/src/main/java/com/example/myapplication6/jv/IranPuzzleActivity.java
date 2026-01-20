package com.example.myapplication6.jv;

import android.content.Intent;
import com.example.myapplication6.ActivityTimer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;

public class IranPuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iran_puzzle);

        EditText input = findViewById(R.id.answerInput);
        Button btn = findViewById(R.id.checkBtn);

        btn.setOnClickListener(v -> {
            String answer = input.getText().toString().trim();

            if (answer.equalsIgnoreCase("tehran")) {


                Toast.makeText(this, "🔥 עברת שלב!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, IranQuestion1Activity.class));
                finish();
            } else {
                Toast.makeText(this, "קוד שגוי", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void goNext() {
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
        startActivity(new Intent(this, IranQuestion1Activity.class));
        finish();    }
}