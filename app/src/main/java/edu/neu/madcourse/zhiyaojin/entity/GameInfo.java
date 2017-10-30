package edu.neu.madcourse.zhiyaojin.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameInfo implements Serializable {

    private String username = "Anonymous";
    private int score = 0;
    private String highestScoreWord = "";
    private int highestScore = 0;
    private String date;

    public GameInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        date = sdf.format(new Date());
    }

    public void updateHighestScoreWord(String word, int score) {
        if (score > highestScore) {
            highestScore = score;
            highestScoreWord = word;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getHighestScoreWord() {
        return highestScoreWord;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public String getDate() {
        return date;
    }
}
