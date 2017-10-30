package edu.neu.madcourse.zhiyaojin.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.fragment.LeaderBoardFragment;
import edu.neu.madcourse.zhiyaojin.fragment.ScoreBoardFragment;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.high_scores_fragment_container, new LeaderBoardFragment());
        ft.commit();

        final Button leaderBoardBtn = (Button)findViewById(R.id.leader_board_btn);
        final Button scoreBoardBtn = (Button)findViewById(R.id.score_board_btn);

        leaderBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.high_scores_fragment_container, new LeaderBoardFragment());
                ft.commit();
            }
        });

        scoreBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.high_scores_fragment_container, new ScoreBoardFragment());
                ft.commit();
            }
        });
    }

}
