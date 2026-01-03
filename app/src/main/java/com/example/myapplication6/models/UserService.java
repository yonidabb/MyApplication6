package com.example.myapplication6.models;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UserService {

    // ===== Singleton =====
    private static volatile UserService instance;
    public interface ScoresCallback {
        void onResult(java.util.List<Score> scores);
    }
    private static final String TAG = "UserService";

    private final FirebaseAuth auth;

    private UserService() {
        auth = FirebaseAuth.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    // ===== AUTH =====
    public void signIn(
            AppCompatActivity activity,
            String email,
            String password,
            Runnable onSuccess
    ) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show();
                        onSuccess.run();
                    } else {
                        Toast.makeText(
                                activity,
                                task.getException() != null
                                        ? task.getException().getMessage()
                                        : "Login failed",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
    public void getTopScoresForUser(String user, ScoresCallback callback) {
        FirebaseFirestore.getInstance()
                .collection("scores")
                .whereEqualTo("user", user)
                .orderBy("score", Query.Direction.ASCENDING)
                .limit(10)
                .get()
                .addOnFailureListener(e -> Log.e(TAG, "getTopScoresForUser", e))
                .addOnSuccessListener(snapshot -> {
                    java.util.List<Score> list = new java.util.ArrayList<>();
                    for (var doc : snapshot.getDocuments()) {
                        Score s = doc.toObject(Score.class);
                        if (s != null) list.add(s);
                    }
                    callback.onResult(list);
                });
    }

    public void getTopScroesForUser(String user) {
        Log.d("UserService", " -> getTopScroesForUser: "+user );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference scoresRef = db.collection("scores");
        db.collection("scores")
                .whereEqualTo("user", user)
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnFailureListener(e -> Log.e(TAG, "failed getTopScroesForUser", e))
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {


                        Log.d("getTopScroesForUser", " -> " + doc.getData());
                    }
                });
        Log.d("UserService", " <- getTopScroesForUser: "+user );

    }

    public void getTopScoresAllTime(ScoresCallback callback) {
        FirebaseFirestore.getInstance()
                .collection("scores")
                .orderBy("score", Query.Direction.ASCENDING)
                .limit(10)
                .get()
                .addOnFailureListener(e -> Log.e(TAG, "getTopScoresAllTime", e))
                .addOnSuccessListener(snapshot -> {
                    java.util.List<Score> list = new java.util.ArrayList<>();
                    for (var doc : snapshot.getDocuments()) {
                        Score s = doc.toObject(Score.class);
                        if (s != null) list.add(s);
                    }
                    callback.onResult(list);
                });
    }



    public void getTopAllTimeScores() {
        Log.d("UserService", " -> getTopAllTimeScores" );

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference scoresRef = db.collection("scores");
        db.collection("scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnFailureListener(e -> Log.e(TAG, "getTopAllTimeScores", e))
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {


                        Log.d("getTopAllTimeScores", " -> " + doc.getData());
                    }
                });
        Log.d("UserService", " <- getTopAllTimeScores" );

    }



    public void registerUser(
            AppCompatActivity activity,
            UserProfile profile,
            Runnable onSuccess
    ) {
        auth.createUserWithEmailAndPassword(
                        profile.getEmail(),
                        profile.getPassword()
                )
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            profile.setUserID(user.getUid());
                            saveProfile(profile);
                        }
                        Toast.makeText(activity, "Registered successfully", Toast.LENGTH_SHORT).show();
                        onSuccess.run();
                    } else {
                        Toast.makeText(
                                activity,
                                task.getException() != null
                                        ? task.getException().getMessage()
                                        : "Registration failed",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    public void sendPasswordResetEmail(
            AppCompatActivity activity,
            String email
    ) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                activity,
                                "Password reset email sent",
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                activity,
                                task.getException() != null
                                        ? task.getException().getMessage()
                                        : "Failed to send reset email",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // ===== FIRESTORE =====
    private void saveProfile(UserProfile profile) {
        FirebaseFirestore.getInstance()
                .collection("profiles")
                .document(profile.getUserID())
                .set(profile)
                .addOnSuccessListener(a -> Log.d(TAG, "Profile saved"))
                .addOnFailureListener(e -> Log.e(TAG, "Profile save error", e));
    }

    public void insertScore(Score score) {
        FirebaseFirestore.getInstance()
                .collection("scores1")
                .document(score.getGameID())
                .set(score)
                .addOnSuccessListener(a -> Log.d(TAG, "Score saved "+score))
                .addOnFailureListener(e -> Log.e(TAG, "Score error", e));
    }


}