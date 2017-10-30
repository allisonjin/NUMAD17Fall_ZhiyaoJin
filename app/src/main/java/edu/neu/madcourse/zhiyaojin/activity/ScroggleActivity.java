package edu.neu.madcourse.zhiyaojin.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.dao.GameDao;
import edu.neu.madcourse.zhiyaojin.dao.UserDao;
import edu.neu.madcourse.zhiyaojin.entity.GameInfo;
import edu.neu.madcourse.zhiyaojin.fragment.ControlFragment;
import edu.neu.madcourse.zhiyaojin.fragment.ScroggleFragment;
import edu.neu.madcourse.zhiyaojin.game.SendMessageAsyncTask;
import edu.neu.madcourse.zhiyaojin.listener.OnGetDataListener;

public class ScroggleActivity extends AppCompatActivity {

    private ScroggleFragment mScroggleFragment;
    private ControlFragment mControlFragment;

    private GameDao mGameDao;
    private UserDao mUserDao;

    private final GameInfo mGameInfo = new GameInfo();

    private CountDownTimerPausable mCountDownTimer;
    private MediaPlayer mMediaPlayer;

    private boolean phase1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scroggle);
        getSupportActionBar().setTitle(R.string.game_title);
        mScroggleFragment = (ScroggleFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_scroggle);
        mControlFragment = (ControlFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_game_controls);

        mGameDao = new GameDao();
        mUserDao = new UserDao(getApplicationContext());

        mUserDao.readLastUsername(new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue() != null ?
                        dataSnapshot.getValue().toString() : "Anonymous";
                mGameInfo.setUsername(username);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });

        mScroggleFragment.setGameInfo(mGameInfo);
        mControlFragment.setGameInfo(mGameInfo);

        mMediaPlayer = MediaPlayer.create(this, R.raw.brave_world);
        mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        mCountDownTimer = new CountDownTimerPausable(90000);
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

    public void updatePoints() {
        mControlFragment.updatePointsTextView();
    }

    public void clearSelectedWord() {
        mScroggleFragment.clearSelections();
    }

    public void startPhase2() {
        phase1 = false;
        mScroggleFragment.startPhase2();
        mCountDownTimer.reset(90000);
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
                        writeGameData();
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

    private void writeGameData() {
        final String gameId = mGameDao.addGame(mGameInfo);
        mUserDao.setLastUsername(mGameInfo.getUsername());
        mUserDao.addUserGame(gameId, mGameInfo);
        mGameDao.getHighestGame(new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<Map<String, GameInfo>> genericTypeIndicator =
                            new GenericTypeIndicator<Map<String, GameInfo>>() {};
                    Map<String, GameInfo> games = dataSnapshot.getValue(genericTypeIndicator);
                    for (String highestGameId : games.keySet()) {
                        Log.i("highest id", highestGameId);
                        if (gameId.equals(highestGameId)) {
                            new SendMessageAsyncTask("scroggle", gameId, "Highest Score!",
                                    "User " + games.get(gameId).getUsername()
                                            + " made the highest score in Scroggle!")
                                    .execute();
                            Log.i("highest", gameId);
                            FirebaseMessaging.getInstance().subscribeToTopic(gameId);
                        }
                    }
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    private void displayGameResult(int score, List<String> wordsFound) {
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("score", score);
        intent.putStringArrayListExtra("wordList", (ArrayList<String>)wordsFound);
        startActivity(intent);
    }

}
