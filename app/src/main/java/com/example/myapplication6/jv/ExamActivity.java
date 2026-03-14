package com.example.myapplication6.jv;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication6.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kahoot-like exam screen:
 * - One question at a time
 * - 4 big colored answer buttons
 * - Timer + progress
 */
public class ExamActivity extends AppCompatActivity {

    private View resultOverlay;
    private android.widget.ImageView resultIcon;
    private TextView resultTitleTv, resultQuestionTv, resultAnswerTv;

    // ===== Views =====
    private TextView qIndexTv, questionTv, scoreTv;
    private TextView timerChip;


    private com.google.android.material.progressindicator.LinearProgressIndicator progressBar;

    private com.google.android.material.button.MaterialButton btnA, btnB, btnC, btnD;
    private Button hintBtn, nextBtn;

    // ===== Data =====
    private ExamModel exam;
    private int currentIndex = 0;
    private int totalScore = 0;

    // ===== State =====
    private boolean locked = false; // lock answers after choice
    private CountDownTimer countDownTimer;
    private long timeLeftMs = 0;

    // ===== Config =====
    private static final String DEFAULT_EXAM_FILE = "exam.json";
    private static final int DEFAULT_TIME_PER_Q_SECONDS = 20; // Kahoot-ish default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        bindViews();

        String documentId = getIntent().getStringExtra("documentId");

        if (documentId != null) {
            loadExamFromFirestore(documentId);
        } else {
            loadExamFromAssets(DEFAULT_EXAM_FILE);
        }

        hintBtn.setOnClickListener(v -> showHint());
        nextBtn.setOnClickListener(v -> goNext());

        btnA.setOnClickListener(v -> onAnswerClicked(btnA));
        btnB.setOnClickListener(v -> onAnswerClicked(btnB));
        btnC.setOnClickListener(v -> onAnswerClicked(btnC));
        btnD.setOnClickListener(v -> onAnswerClicked(btnD));
    }

    private void loadExamFromFirestore(String documentId) {
        FirebaseFirestore.getInstance().collection("exams")
                .document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String json = documentSnapshot.getString("content");
                    if (json != null) {
                        initExam(json);
                    } else {
                        Toast.makeText(this, "Exam content is empty", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load from Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void loadExamFromAssets(String fileName) {
        try {
            String json = readAssetFile(fileName);
            initExam(json);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load from assets: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initExam(String json) {
        try {
            exam = ExamParser.parse(json);

            if (exam.shuffleQuestions) Collections.shuffle(exam.questions);
            if (exam.shuffleOptions) {
                for (QuestionModel q : exam.questions) Collections.shuffle(q.options);
            }

            progressBar.setMax(exam.questions.size());
            renderQuestion();

        } catch (Exception e) {
            Toast.makeText(this, "Parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void bindViews() {
        qIndexTv = findViewById(R.id.qIndexTv);
        questionTv = findViewById(R.id.questionTv);
        scoreTv = findViewById(R.id.scoreTv);

        timerChip = findViewById(R.id.timerChip);
        progressBar = findViewById(R.id.progressBar);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        hintBtn = findViewById(R.id.hintBtn);
        nextBtn = findViewById(R.id.nextBtn);

        resultOverlay = findViewById(R.id.resultOverlay);
        resultIcon = findViewById(R.id.resultIcon);
        resultTitleTv = findViewById(R.id.resultTitleTv);
        resultQuestionTv = findViewById(R.id.resultQuestionTv);
        resultAnswerTv = findViewById(R.id.resultAnswerTv);
    }

    // ========================= Rendering =========================

    private void renderQuestion() {
        locked = false;
        nextBtn.setEnabled(false);

        QuestionModel q = exam.questions.get(currentIndex);

        qIndexTv.setText("שאלה " + (currentIndex + 1) + "/" + exam.questions.size());
        questionTv.setText(q.question);

        progressBar.setProgress(currentIndex + 1);

        // set button text + tag with optionId
        setAnswerButton(btnA, q, 0);
        setAnswerButton(btnB, q, 1);
        setAnswerButton(btnC, q, 2);
        setAnswerButton(btnD, q, 3);

        // Hint visibility
        if (!TextUtils.isEmpty(q.hint)) {
            hintBtn.setVisibility(View.VISIBLE);
        } else {
            hintBtn.setVisibility(View.GONE);
        }

        // start timer per question
        startQuestionTimer(getTimeForQuestionSeconds(q));
        animateQuestionIn();
    }

    private void setAnswerButton(com.google.android.material.button.MaterialButton btn,
                                 QuestionModel q, int index) {
        if (index < q.options.size()) {
            OptionModel o = q.options.get(index);
            btn.setVisibility(View.VISIBLE);
            btn.setText(o.id + ") " + o.text);
            btn.setTag(o.id);
            btn.setEnabled(true);
            btn.setAlpha(1f);

            // reset strokes from previous question
            btn.setStrokeWidth(0);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private String getCorrectAnswerText(QuestionModel q) {
        for (OptionModel o : q.options) {
            if (o.id != null && o.id.equals(q.correctOptionId)) {
                return o.id + ") " + o.text;
            }
        }
        return q.correctOptionId; // fallback
    }

    private void showResultOverlay(boolean correct, QuestionModel q) {
        String correctText = getCorrectAnswerText(q);

        resultQuestionTv.setText("שאלה: " + q.question);
        resultAnswerTv.setText("התשובה הנכונה: " + correctText);

        if (correct) {
            resultIcon.setImageResource(R.drawable.ic_check_circle_64);
            resultTitleTv.setText("תשובה נכונה!");
        } else {
            resultIcon.setImageResource(R.drawable.ic_close_circle_64);
            resultTitleTv.setText("טעות!");
        }

        resultOverlay.setAlpha(0f);
        resultOverlay.setVisibility(View.VISIBLE);
        resultOverlay.animate().alpha(1f).setDuration(120).start();
    }

    private void hideResultOverlay() {
        resultOverlay.animate().alpha(0f).setDuration(500).withEndAction(() -> {
            resultOverlay.setVisibility(View.GONE);
            resultOverlay.setAlpha(1f);
        }).start();
    }


    private int getTimeForQuestionSeconds(QuestionModel q) {
        // If you later add per-question time in JSON, plug it here.
        return DEFAULT_TIME_PER_Q_SECONDS;
    }

    private void animateQuestionIn() {
        questionTv.setScaleX(0.98f);
        questionTv.setScaleY(0.98f);
        questionTv.setAlpha(0f);
        questionTv.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(220)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    // ========================= Answer Logic =========================

    private void onAnswerClicked(com.google.android.material.button.MaterialButton clickedBtn) {
        if (locked) return;

        locked = true;
        stopTimer();

        QuestionModel q = exam.questions.get(currentIndex);
        String chosenId = String.valueOf(clickedBtn.getTag());
        q.selectedOptionId = chosenId;

        boolean correct = chosenId.equals(q.correctOptionId);
        if (correct) totalScore += q.score;

        scoreTv.setText("ניקוד: " + totalScore);

        highlightCorrectAndChosen(q, chosenId);

        showResultOverlay(correct, q);

        nextBtn.setEnabled(true);
        nextBtn.postDelayed(() -> {
            hideResultOverlay();
            goNext();
        }, 950);
    }

    private void highlightCorrectAndChosen(QuestionModel q, String chosenId) {
        // dim all
        dimButton(btnA);
        dimButton(btnB);
        dimButton(btnC);
        dimButton(btnD);

        // highlight correct
        highlightIfMatches(btnA, q.correctOptionId, true);
        highlightIfMatches(btnB, q.correctOptionId, true);
        highlightIfMatches(btnC, q.correctOptionId, true);
        highlightIfMatches(btnD, q.correctOptionId, true);

        // if chosen wrong -> mark chosen too
        if (!TextUtils.isEmpty(chosenId) && !chosenId.equals(q.correctOptionId)) {
            highlightIfMatches(btnA, chosenId, false);
            highlightIfMatches(btnB, chosenId, false);
            highlightIfMatches(btnC, chosenId, false);
            highlightIfMatches(btnD, chosenId, false);
        }
    }

    private void dimButton(com.google.android.material.button.MaterialButton b) {
        b.setAlpha(0.55f);
        b.setEnabled(false);
        b.setStrokeWidth(0);
    }

    private void highlightIfMatches(com.google.android.material.button.MaterialButton b,
                                    String optionId, boolean isCorrect) {
        Object tag = b.getTag();
        if (tag == null) return;

        if (String.valueOf(tag).equals(optionId)) {
            b.setAlpha(1f);
            b.setEnabled(false);
            b.setStrokeWidth(isCorrect ? dp(4) : dp(2));
            b.setStrokeColorResource(isCorrect ? android.R.color.white : android.R.color.black);
        }
    }

    // ========================= Next / Finish =========================

    private void goNext() {
        if (exam == null) return;

        if (currentIndex < exam.questions.size() - 1) {
            currentIndex++;
            renderQuestion();
        } else {
            finishExam();
        }
    }

    private void finishExam() {
        stopTimer();

        int possible = 0;
        int unanswered = 0;
        for (QuestionModel q : exam.questions) {
            possible += q.score;
            if (TextUtils.isEmpty(q.selectedOptionId)) unanswered++;
        }

        String msg = "ניקוד: " + totalScore + " / " + possible +
                (unanswered > 0 ? ("\n\nשאלות שלא נענו: " + unanswered) : "");

        new AlertDialog.Builder(this)
                .setTitle("סיימת!")
                .setMessage(msg)
                .setPositiveButton("סגור", (d, w) -> finish())
                .show();
    }

    private void showHint() {
        QuestionModel q = exam.questions.get(currentIndex);
        if (TextUtils.isEmpty(q.hint)) return;

        new AlertDialog.Builder(this)
                .setTitle("רמז")
                .setMessage(q.hint)
                .setPositiveButton("הבנתי", null)
                .show();
    }

    // ========================= Timer =========================

    private void startQuestionTimer(int seconds) {
        stopTimer();
        timeLeftMs = seconds * 1000L;
        timerChip.setText(seconds + "s");

        countDownTimer = new CountDownTimer(timeLeftMs, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMs = millisUntilFinished;
                long s = Math.max(0, millisUntilFinished / 1000L);
                timerChip.setText(s + "s");
            }

            @Override
            public void onFinish() {
                timeLeftMs = 0;
                timerChip.setText("0s");
                onTimeUp();
            }
        }.start();
    }

    private void onTimeUp() {
        if (locked) return;
        locked = true;

        QuestionModel q = exam.questions.get(currentIndex);
        q.selectedOptionId = null;

        // show correct
        highlightCorrectAndChosen(q, "");
        showResultOverlay(false, q);

        nextBtn.setEnabled(true);
        nextBtn.postDelayed(() -> {
            hideResultOverlay();
            goNext();
        }, 950);
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    // ========================= JSON Load =========================

    private String readAssetFile(String fileName) throws Exception {
        InputStream is = getAssets().open(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append("\n");
        br.close();
        return sb.toString();
    }

    // ========================= Models =========================

    private static class ExamModel {
        String title;
        String description;
        boolean shuffleQuestions;
        boolean shuffleOptions;
        List<QuestionModel> questions = new ArrayList<>();
    }

    private static class QuestionModel {
        String id;
        String question;
        List<OptionModel> options = new ArrayList<>();
        String correctOptionId;
        int score;
        String hint; // optional

        // runtime
        String selectedOptionId;
    }

    private static class OptionModel {
        String id;   // "A"/"B"/"C"/"D"
        String text;
    }

    // ========================= Parser =========================

    private static class ExamParser {
        static ExamModel parse(String json) throws Exception {
            JSONObject root = new JSONObject(json);
            JSONObject examObj = root.getJSONObject("exam");

            ExamModel exam = new ExamModel();
            exam.title = examObj.optString("title", "מבחן");
            exam.description = examObj.optString("description", "");
            exam.shuffleQuestions = examObj.optBoolean("shuffleQuestions", false);
            exam.shuffleOptions = examObj.optBoolean("shuffleOptions", false);

            JSONArray questionsArr = examObj.getJSONArray("questions");
            for (int i = 0; i < questionsArr.length(); i++) {
                JSONObject qObj = questionsArr.getJSONObject(i);

                QuestionModel q = new QuestionModel();
                q.id = qObj.optString("id", "q" + (i + 1));
                q.question = qObj.getString("question");
                q.correctOptionId = qObj.getString("correctOptionId");
                q.score = qObj.optInt("score", 0);
                q.hint = qObj.optString("hint", null);

                JSONArray optionsArr = qObj.getJSONArray("options");
                for (int j = 0; j < optionsArr.length(); j++) {
                    JSONObject oObj = optionsArr.getJSONObject(j);
                    OptionModel o = new OptionModel();
                    o.id = oObj.getString("id");
                    o.text = oObj.getString("text");
                    q.options.add(o);
                }

                exam.questions.add(q);
            }

            return exam;
        }
    }

    // ========================= Utils =========================

    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
