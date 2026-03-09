package com.example.myapplication6.jv;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;
import com.example.myapplication6.models.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {
    private final UserService userService = UserService.getInstance();

    private RecyclerView userRecycler, globalRecycler;
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

        userAdapter.setShowUser(false);
        globalAdapter.setShowUser(true);

        userRecycler.setAdapter(userAdapter);
        globalRecycler.setAdapter(globalAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userKey = user.getUid();
            loadUserScores(userKey);
        } else {
            userAdapter.setItems(new ArrayList<>());
        }

        loadGlobalScores();
    }

    private void saveScore(String user, long score) {
        userService.insertScore(new Score((int) score, user));
    }

    private void loadUserScores(String user) {
        userService.getTopScoresForUser(user, scores -> userAdapter.setItems(scores));
    }

    private void loadGlobalScores() {
        userService.getTopScoresAllTime(scores -> globalAdapter.setItems(scores));
    }
}