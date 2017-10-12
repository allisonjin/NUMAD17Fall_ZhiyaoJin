package edu.neu.madcourse.zhiyaojin.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.fragment.ControlFragment;
import edu.neu.madcourse.zhiyaojin.fragment.ScroggleFragment;

public class ScroggleActivity extends AppCompatActivity {

    private ScroggleFragment mScroggleFragment;
    private ControlFragment mControlFragment;
    private boolean phase1 = true;
    private CountDownTimerPausable countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle);
        mScroggleFragment = (ScroggleFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_scroggle);
        mControlFragment = (ControlFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_game_controls);

        countDownTimer = new CountDownTimerPausable(120000);
        countDownTimer.start();
    }

    public void submitWord() {
        mScroggleFragment.submitWord();
    }

    public void updateWord(String newWord) {
        mControlFragment.updateWordTextView(newWord);
    }

    public void updatePoints(int point) {
        mControlFragment.updatePointsTextView(point);
    }

    public void clearSelectedWord() {
        mScroggleFragment.clearSelectedWord();
    }

    public void startPhase2() {
        phase1 = false;
        mScroggleFragment.startPhase2();
        countDownTimer.reset(60000);
        countDownTimer.start();
    }

    public void pauseGame() {
        countDownTimer.pause();
    }

    public void resumeGame() {
        countDownTimer.start();
    }

    private class CountDownTimerPausable {
        private long totalTime;
        private CountDownTimer countDownTimer = null;

        public CountDownTimerPausable(long totalTime) {
            this.totalTime = totalTime;
        }

        public void start() {
            countDownTimer = new CountDownTimer(totalTime + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    totalTime = millisUntilFinished;
                    mControlFragment.updateTimer(formatSecondsToMMSS(totalTime));
                }

                @Override
                public void onFinish() {
                    mControlFragment.updateTimer(formatSecondsToMMSS(0));
                    if (phase1) {
                        startPhase2();
                        phase1 = false;
                    }
                }
            };
            countDownTimer.start();
        }

        public void pause() {
            countDownTimer.cancel();
        }

        public void reset(long totalTime) {
            this.totalTime = totalTime;
        }

        private String formatSecondsToMMSS(long milliseconds) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
            return formatter.format(milliseconds);
        }
    }

}
