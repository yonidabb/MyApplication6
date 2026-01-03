package com.example.myapplication6.jv;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;
import com.example.myapplication6.models.UserService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    private final UserService userService = UserService.getInstance();

    private RecyclerView userRecycler, globalRecycler;
    private final List<Score> userScores = new ArrayList<>();
    private final List<Score> globalScores = new ArrayList<>();
    private ScoreAdapter userAdapter, globalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        userRecycler = findViewById(R.id.userScoresRecycler);
        globalRecycler = findViewById(R.id.globalScoresRecycler);

        userRecycler.setLayoutManager(new LinearLayoutManager(this));
        globalRecycler.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new ScoreAdapter();
        globalAdapter = new ScoreAdapter();

        userRecycler.setAdapter(userAdapter);
        globalRecycler.setAdapter(globalAdapter);

        // ⬇️ קבלת תוצאה מהטיימר
//        long finalScore = getIntent().getLongExtra("FINAL_SCORE", -1);
//        String userName = getIntent().getStringExtra("USER_NAME");
//
//        if (finalScore != -1 && userName != null) {
//            saveScore(userName, finalScore);
//        }

        String userName="yoni";
        loadUserScores(userName);
//        userService.getTopScroesForUser(userName);
//        userService.getTopAllTimeScores();
        loadGlobalScores();
    }


    private void saveScore(String user, long score) {
        userService.insertScore(new Score((int) score, user));
    }
    private void loadUserScores(String user) {
        userService.getTopScoresForUser(user, scores -> {
            userAdapter.setItems(scores);
        });
    }



    private void loadGlobalScores() {
        userService.getTopScoresAllTime(scores -> {
            globalAdapter.setItems(scores);
        });
    }
}