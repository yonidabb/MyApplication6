package com.example.myapplication6.models;

import java.util.Date;
import java.util.UUID;

public class Score {

    private long score;      // <-- שים לב: זה בשניות
    private String user;
    private Date date;
    private String gameID;

    public Score() {
        // חובה ל-Firestore
    }

    public Score(long score, String user) {
        this.score = score;
        this.user = user;
        this.gameID = UUID.randomUUID().toString();
        this.date = new Date();
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