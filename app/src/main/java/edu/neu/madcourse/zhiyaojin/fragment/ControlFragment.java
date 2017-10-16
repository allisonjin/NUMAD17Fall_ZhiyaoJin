package edu.neu.madcourse.zhiyaojin.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.activity.ScroggleActivity;

public class ControlFragment extends Fragment {

    private TextView wordTextView;
    private TextView timerTextView;
    private TextView pointsTextView;
    private ImageButton pauseButton;
    private ImageButton musicButton;
    private ImageButton infoButton;
    private boolean gameRunning = true;
    private boolean musicPlaying = true;

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
        pauseButton = rootView.findViewById(R.id.pause_btn);
        musicButton = rootView.findViewById(R.id.music_btn);
        infoButton = rootView.findViewById(R.id.game_info_btn);

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
                toggleGamePause();
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlaying) {
                    ((ScroggleActivity)getActivity()).pauseMusic();
                } else {
                    ((ScroggleActivity)getActivity()).resumeMusic();
                }
                toggleButton(musicButton, musicPlaying);
                musicPlaying = !musicPlaying;
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameRunning) {
                    toggleGamePause();
                }
                String instructions = new StringBuilder()
                        .append("Scroggle game has two phases.\n\n")
                        .append("Phase 1: \n")
                        .append("You need to find the longest word they can in each Boggle board\n\n")
                        .append("Phase 2: \n")
                        .append("Pick letters from each board. You can revisit grids, ")
                        .append("but not back to back.\n\n")
                        .append("Find as many words as you can!")
                        .toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Help")
                        .setMessage(instructions)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toggleGamePause();
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
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

    private void toggleButton(ImageButton button, boolean isFirstState) {
        Drawable drawable = button.getBackground();
        if (isFirstState) {
            drawable.setLevel(1);
        } else {
            drawable.setLevel(0);
        }
    }

    private void toggleGamePause() {
        if (gameRunning) {
            ((ScroggleActivity)getActivity()).pauseGame();
        } else {
            ((ScroggleActivity)getActivity()).resumeGame();
        }
        toggleButton(pauseButton, gameRunning);

        gameRunning = !gameRunning;
    }

    public void setTimerColor(int colorId) {
        timerTextView.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

}
