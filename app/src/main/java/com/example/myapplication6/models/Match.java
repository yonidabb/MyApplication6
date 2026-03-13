package com.example.myapplication6.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.UUID;

public class Match {

    private String userid;
    private String user;
    private Date date;
    private String gameID;

    public Match() {
        // חובה ל-Firestore
    }


    public Match(FirebaseUser user) {
        this.userid = user.getUid();
        this.user = user.getDisplayName();
        this.gameID = UUID.randomUUID().toString();
        this.date = new Date();
    }

    public String getUserid() {
        return userid;
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
                ", user='" + user + '\'' +
                ", date=" + date +
                ", gameID='" + gameID + '\'' +
                '}';
    }
}