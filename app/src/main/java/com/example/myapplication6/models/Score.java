package com.example.myapplication6.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.UUID;

public class Score {

    private String userid;


    private long score;      // <-- שים לב: זה בשניות
    private String user;
    private Date date;
    private String gameID;

    public Score() {
        // חובה ל-Firestore
    }

    public Score(long score, FirebaseUser user) {
        this.score = score;
        this.user = user.getDisplayName();
        this.userid = user.getUid();
        this.gameID = UUID.randomUUID().toString();
        this.date = new Date();
    }

    public String getUserid() {
        return userid;
    }


    public long getScore() {
        return score; // זה כבר בשניות
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", user='" + user + '\'' +
                ", date=" + date +
                ", gameID='" + gameID + '\'' +
                '}';
    }
}