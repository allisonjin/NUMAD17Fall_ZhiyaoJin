package edu.neu.madcourse.zhiyaojin.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.fragment.ControlFragment;
import edu.neu.madcourse.zhiyaojin.fragment.ScroggleFragment;

public class ScroggleActivity extends AppCompatActivity {

    private ScroggleFragment mScroggleFragment;
    private ControlFragment mControlFragment;
    private boolean phase1 = true;
    private CountDownTimerPausable mCountDownTimer;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle);
        getSupportActionBar().setTitle(R.string.game_title);
        mScroggleFragment = (ScroggleFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_scroggle);
        mControlFragment = (ControlFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_game_controls);

        mMediaPlayer = MediaPlayer.create(this, R.raw.brave_world);
        mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        mCountDownTimer = new CountDownTimerPausable(40000);
        mCountDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mCountDownTimer.pause();
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
        mCountDownTimer.reset(40000);
        mCountDownTimer.start();
    }

    public void pauseGame() {
        mCountDownTimer.pause();
        mScroggleFragment.toggleTilesVisibility();
    }

    public void resumeGame() {
        mScroggleFragment.toggleTilesVisibility();
        mCountDownTimer.start();
    }

    public void pauseMusic() {
        mMediaPlayer.pause();
    }

    public void resumeMusic() {
        mMediaPlayer.start();
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

                    if (totalTime < 30000 && totalTime >= 29000) {
                        mControlFragment.setTimerColor(R.color.red_color);
                    }
                }

                @Override
                public void onFinish() {
                    mControlFragment.updateTimer(formatSecondsToMMSS(0));
                    mControlFragment.setTimerColor(R.color.dark_border_color);
                    if (phase1) {
                        phase1 = false;
                        startPhase2();
                    } else {
                        int score = mScroggleFragment.getScore();
                        List<String> wordsFound = mScroggleFragment.getWordsFound();
                        displayGameResult(score, wordsFound);
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

    private void displayGameResult(int score, List<String> wordsFound) {
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("score", score);
        intent.putStringArrayListExtra("wordList", (ArrayList<String>)wordsFound);
        startActivity(intent);
    }

}
