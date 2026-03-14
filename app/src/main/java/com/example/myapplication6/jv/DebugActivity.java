package com.example.myapplication6.jv;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.ActivityTimer;
import com.example.myapplication6.R;
import com.example.myapplication6.models.ExamData;
import com.example.myapplication6.models.UserService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DebugActivity extends AppCompatActivity {

    private TextView resultText;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private static final String TAG = "DebugActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        resultText = findViewById(R.id.debugResultText);

        Button btnTimer = findViewById(R.id.btnTestTimer);
        Button btnFirebase = findViewById(R.id.btnTestFirebase);
        Button btnSound = findViewById(R.id.btnTestSound);
        Button btnCreateKahoot = findViewById(R.id.btnCreateKahoot);
        Button btnLoadKahoot = findViewById(R.id.btnLoadKahoot);
        Button btnClear = findViewById(R.id.btnClear);

        ActivityTimer activityTimer = ActivityTimer.getInstance();


        btnFirebase.setOnClickListener(v ->
                {
                    // Random number between 50 and 400
                    long timeLeftMillis = new Random().nextInt(400 - 50 + 1) + 50;
                    timeLeftMillis *= 1000;

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

        btnCreateKahoot.setOnClickListener(v -> {
            showKahootCreationDialog();
        });

        btnLoadKahoot.setOnClickListener(v -> {
            startActivity(new Intent(this, ExamListActivity.class));
        });

        btnClear.setOnClickListener(v ->
                resultText.setText("Debug output cleared"));
    }

    private void showKahootCreationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create AI Kahoot Game");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText inputSubject = new EditText(this);
        inputSubject.setHint("Enter Subject (e.g. History of Rome)");
        inputSubject.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputSubject);

        final TextView labelComplexity = new TextView(this);
        labelComplexity.setText("Select Complexity:");
        labelComplexity.setPadding(0, 20, 0, 0);
        layout.addView(labelComplexity);

        final Spinner spinnerComplexity = new Spinner(this);
        String[] complexities = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, complexities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComplexity.setAdapter(adapter);
        layout.addView(spinnerComplexity);

        builder.setView(layout);

        builder.setPositiveButton("Generate", (dialog, which) -> {
            String subject = inputSubject.getText().toString();
            String complexity = spinnerComplexity.getSelectedItem().toString();
            if (!subject.isEmpty()) {
                generateExamWithVertexAI(subject, complexity);
            } else {
                Toast.makeText(this, "Subject cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void generateExamWithVertexAI(String subject, String complexity) {
        log("Generating exam for: " + subject + " (" + complexity + ")... ⏳");

        // Use gemini-2.0-flash for better stability
        GenerativeModel gm = FirebaseVertexAI.getInstance()
                .generativeModel("gemini-2.5-flash");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        String structure = "{\"version\":\"1.0\",\"exam\":{\"id\":\"string\",\"title\":\"string\",\"description\":\"string\",\"timeLimitSeconds\":900,\"questions\":[{\"id\":\"q1\",\"question\":\"string\",\"options\":[{\"id\":\"A\",\"text\":\"string\"}],\"correctOptionId\":\"A\",\"score\":10}]}}";

        String prompt = "Create a new exam in JSON format about the subject: " + subject + ". " +
                "The complexity should be " + complexity + ". " +
                "The response must be in the same language as the subject provided. " +
                "Use this exact structure: " + structure + ". " +
                "Provide 10 questions. Return ONLY the JSON, no markdown formatting.";

        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultTextJson = cleanJson(result.getText());
                Log.d(TAG, "VertexAI Result cleaned: " + resultTextJson);
                validateAndProcessExam(subject, complexity, resultTextJson, 0);
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    log("VertexAI Error: " + t.getMessage());
                    Log.e(TAG, "Error in generateContent", t);
                });
            }
        }, executor);
    }

    private String cleanJson(String json) {
        if (json == null) return "";
        json = json.trim();
        if (json.startsWith("```json")) {
            json = json.substring(7);
        } else if (json.startsWith("```")) {
            json = json.substring(3);
        }
        if (json.endsWith("```")) {
            json = json.substring(0, json.length() - 3);
        }
        return json.trim();
    }

    private void validateAndProcessExam(String subject, String complexity, String json, int retryCount) {
        try {
            // Validate parsing
            Log.d(TAG, "Validating JSON for subject: " + subject);
            ExamData.ExamParser.parse(json);
            log("JSON validated successfully! ✅");
            saveExamToFirestore(subject, complexity, json);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            Log.e(TAG, "Validation error: " + errorMsg + "\nJSON: " + json);
            if (retryCount < 2) {
                runOnUiThread(() -> log("Validation failed ⚠️ (Attempt " + (retryCount + 1) + "): " + errorMsg + ". Retrying fix with AI..."));
                fixExamWithAI(subject, complexity, json, errorMsg, retryCount);
            } else {
                runOnUiThread(() -> log("Generation failed after all attempts ❌. Logging to failure collection."));
                logFailedExam(subject, complexity, json, errorMsg);
            }
        }
    }

    private void fixExamWithAI(String subject, String complexity, String badJson, String errorMsg, int retryCount) {
        GenerativeModel gm = FirebaseVertexAI.getInstance()
                .generativeModel("gemini-2.0-flash");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        String structure = "{\"version\":\"1.0\",\"exam\":{\"id\":\"string\",\"title\":\"string\",\"description\":\"string\",\"timeLimitSeconds\":900,\"questions\":[{\"id\":\"q1\",\"question\":\"string\",\"options\":[{\"id\":\"A\",\"text\":\"string\"}],\"correctOptionId\":\"A\",\"score\":10}]}}";

        String promptPrefix = retryCount == 1 ? "FINAL ATTEMPT: " : "";
        String prompt = promptPrefix + "The following JSON generated for subject '" + subject + "' failed validation with error: " + errorMsg + ". " +
                "Please FIX the JSON to strictly follow this structure: " + structure + ". " +
                "Ensure it is valid JSON and contains 10 questions. Return ONLY the fixed JSON string. " +
                "Bad JSON: " + badJson;

        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String fixedJson = cleanJson(result.getText());
                validateAndProcessExam(subject, complexity, fixedJson, retryCount + 1);
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    log("Fix attempt " + (retryCount + 1) + " failed ❌: " + t.getMessage());
                    Log.e(TAG, "Error in fixExamWithAI", t);
                });
            }
        }, executor);
    }

    private void logFailedExam(String subject, String complexity, String badJson, String errorMsg) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : "anonymous";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> failedData = new HashMap<>();
        failedData.put("subject", subject);
        failedData.put("complexity", complexity);
        failedData.put("invalidJson", badJson);
        failedData.put("errorMessage", errorMsg);
        failedData.put("createdBy", uid);
        failedData.put("createdAt", com.google.firebase.Timestamp.now());

        db.collection("failed_exams")
                .add(failedData)
                .addOnSuccessListener(doc -> runOnUiThread(() -> {
                    log("Failure logged to Firestore (ID: " + doc.getId() + ") 📄");
                    Toast.makeText(this, "Generation failed twice. Logged for review.", Toast.LENGTH_LONG).show();
                }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error logging failure", e);
                    runOnUiThread(() -> log("Error logging failure: " + e.getMessage()));
                });
    }

    private void saveExamToFirestore(String subject, String complexity, String jsonContent) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            runOnUiThread(() -> log("Error: User not logged in"));
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> examData = new HashMap<>();
        examData.put("subject", subject);
        examData.put("complexity", complexity);
        examData.put("createdBy", user.getUid());
        examData.put("createdAt", com.google.firebase.Timestamp.now());
        examData.put("content", jsonContent);

        db.collection("exams")
                .add(examData)
                .addOnSuccessListener(documentReference -> {
                    runOnUiThread(() -> {
                        String docId = documentReference.getId();
                        log("Exam saved to Firestore! ID: " + docId + " ✅");
                        Toast.makeText(DebugActivity.this, "Exam created successfully! Launching for validation...", Toast.LENGTH_LONG).show();

                        // Launch ExamActivity for validation
                        Intent intent = new Intent(DebugActivity.this, ExamActivity.class);
                        intent.putExtra("documentId", docId);
                        startActivity(intent);
                    });
                })
                .addOnFailureListener(e -> {
                    runOnUiThread(() -> {
                        log("Firestore Error: " + e.getMessage());
                        Log.e(TAG, "Error in saveExamToFirestore", e);
                    });
                });
    }

    private void log(String message) {
        runOnUiThread(() -> {
            String current = resultText.getText().toString();
            resultText.setText(current + "\n" + message);
        });
    }
}
