package edu.neu.madcourse.zhiyaojin.entity;

public class User {

    private String userId;
    private String username = "Anonymous";

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
