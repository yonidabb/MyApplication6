package com.example.myapplication6.models;

import java.util.Date;
import java.util.UUID;

public class Score {

    private int score;
    private String user;
    private Date date;

    public Score() {
    }

    public Score(int score, String user) {
        this.score = score;
        this.user = user;
        gameID = UUID.randomUUID().toString();
        date = new Date();
    }

    private String gameID;

    public Date getDate() {
        return date;
    }

    public String getID() {
        return null;
    }

    public int getScore() {
        return score;
    }

    public String getUser() {
        return user;
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