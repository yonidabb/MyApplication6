package com.example.myapplication6.models;

import java.util.Date;
import java.util.UUID;

public class Score {

    private long score;
    private String user;
    private Date date;

    public Score() {
    }

    public Score(long score, String user) {
        this.score = score;
        this.user = user;
        gameID = UUID.randomUUID().toString();
        date = new Date();
    }

    private String gameID;

    public Date getDate() {
        return date;
    }

//    public String getID() {
//        return null;
//    }

    public long getScore() {
        return score;
    }

    public long getScoreInSeconds() {
        return score/1000;
    }

    public String getUser() {
        return user;
    }

    public String getID() {
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