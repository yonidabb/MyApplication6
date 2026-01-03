package com.example.myapplication6.models;

public class UserProfile {

    private String name;
    private String email;
    private String age;
    private String state;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;

//    public UserProfile() {
//        // חובה ל-Firebase
//    }

    public UserProfile(String name, String email, String age, String state, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.state = state;
        this.userID=userID;
        this.password=password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

}