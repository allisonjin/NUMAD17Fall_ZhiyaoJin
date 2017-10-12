package edu.neu.madcourse.zhiyaojin.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.activity.ScroggleActivity;


public class ControlFragment extends Fragment {

    private TextView wordTextView;
    private TextView timerTextView;
    private TextView pointsTextView;
    private boolean gamePaused = false;

    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        wordTextView = rootView.findViewById(R.id.word);
        timerTextView = rootView.findViewById(R.id.timer);
        pointsTextView = rootView.findViewById(R.id.points);
        pointsTextView.setText("0");

        ImageButton submitButton = rootView.findViewById(R.id.word_submit_btn);
        ImageButton cancelButton = rootView.findViewById(R.id.word_cancel_btn);
        ImageButton pauseButton = rootView.findViewById(R.id.pause_btn);
        ImageButton musicButton = rootView.findViewById(R.id.music_btn);
        ImageButton infoButton = rootView.findViewById(R.id.game_info_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScroggleActivity)getActivity()).submitWord();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ScroggleActivity)getActivity()).clearSelectedWord();
                updateWordTextView("");
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamePaused) {
                    ((ScroggleActivity)getActivity()).resumeGame();
                } else {
                    ((ScroggleActivity)getActivity()).pauseGame();
                }
                gamePaused = !gamePaused;
            }
        });
        return rootView;
    }

    public void updateWordTextView(String newWord) {
        wordTextView.setText(newWord.toUpperCase());
    }

    public void updateTimer(String timeLeft) {
        timerTextView.setText(timeLeft);
    }

    public void updatePointsTextView(int points) {
        pointsTextView.setText(String.valueOf(points));
    }

}
