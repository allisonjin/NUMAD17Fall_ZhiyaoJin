package edu.neu.madcourse.zhiyaojin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.neu.madcourse.zhiyaojin.R;

public class GameMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        getSupportActionBar().setTitle(R.string.game_title);
    }
}
