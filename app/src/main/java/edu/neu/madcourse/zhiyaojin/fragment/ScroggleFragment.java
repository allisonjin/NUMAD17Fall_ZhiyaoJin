package edu.neu.madcourse.zhiyaojin.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.activity.ScroggleActivity;
import edu.neu.madcourse.zhiyaojin.dictionary.DictionaryDBHelper;
import edu.neu.madcourse.zhiyaojin.entity.GameInfo;
import edu.neu.madcourse.zhiyaojin.game.BoggleBoardGenerator;
import edu.neu.madcourse.zhiyaojin.game.Tile;

public class ScroggleFragment extends Fragment {

    private static int[] mLargeIds = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    private static int[] mSmallIds = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};
    private static int[] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1,
            1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

    private final static int BOARD_SIZE = 3;
    private final static int WORD_LENGTH = BOARD_SIZE * BOARD_SIZE;
    private final static int BONUS = 10;
    private final static String PHASE_1 = "phase1";
    private final static String PHASE_2 = "phase2";

    private MediaPlayer mMediaPlayer;
    private DictionaryDBHelper mDBHelper;
    private BoggleBoardGenerator mBoardGenerator;

    private Tile mEntireBoard = new Tile(this);
    private Tile[] mLargeTiles = new Tile[9];
    private Tile[][] mSmallTiles = new Tile[9][9];

    private GameInfo mGameInfo;

    private String word = "";
    List<String> wordsFound = new ArrayList<>();
    private int mLastLarge;
    private int mLastSmall;

    private String phase = PHASE_1;

    public ScroggleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new DictionaryDBHelper(getContext());
        mBoardGenerator = new BoggleBoardGenerator(mDBHelper, BOARD_SIZE);
        setRetainInstance(true);
        initGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.large_board, container, false);
        initViews(rootView);
        updateAllTiles();
        return rootView;
    }

//    public GameInfo getGameInfo() {
//        return mGameInfo;
//    }

    public void setGameInfo(GameInfo mGameInfo) {
        this.mGameInfo = mGameInfo;
    }

    private void disableTiles(Tile... tiles) {
        for (Tile t : tiles) {
            t.disable();
            if (t.getSubTiles() != null) {
                disableTiles(t.getSubTiles());
            }
        }
    }

    private void enableTiles() {
        for (Tile largeTile : mLargeTiles) {
            if (largeTile.isAvailable()) {
                enableTiles(largeTile);
            }
        }
    }

    private void enableTiles(Tile... tiles) {
        for (Tile tile : tiles) {
            if (!tile.isAvailable()) {
                tile.enable();
            }
            if (tile.getSubTiles() != null) {
                enableTiles(tile.getSubTiles());
            }
        }
    }

    private boolean isPhase2() {
        return phase.equals(PHASE_2);
    }

    private void resetLastMove() {
        mLastSmall = -1;
        mLastLarge = -1;
    }

    public void initGame() {
        initGameInfo();
        mEntireBoard = new Tile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            for (int small = 0; small < 9; small++) {
                Tile tile = new Tile(this);
                mSmallTiles[large][small] = tile;
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);
        resetLastMove();
        new GameBoardGenerationTask().execute();
    }

    private void initGameInfo() {
        mGameInfo = new GameInfo();
    }

    private class GameBoardGenerationTask extends AsyncTask<Void, Void, char[][]> {

        @Override
        protected char[][] doInBackground(Void... params) {
            char[][] boards = new char[9][9];
            for (int i = 0; i < 9; i++) {
                boards[i] = mBoardGenerator.getRandomBoard();
            }
            return boards;
        }

        @Override
        protected void onPostExecute(char[][] boards) {
            for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    Tile tile = mSmallTiles[large][small];
                    tile.setValue(String.valueOf(boards[large][small]));
                }
            }
            updateAllTiles();
        }
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for (int small = 0; small < 9; small++) {
                Button inner = outer.findViewById(mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final Tile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);

                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (nextMoveAvailable(fLarge, fSmall)) {
                            makeMove(fLarge, fSmall);
                            playClickSound();
                        }
                    }
                });
            }
        }
    }

    public List<String> getWordsFound() {
        return wordsFound;
    }

    public int getScore() {
        return mGameInfo.getScore();
    }

    public void startPhase2() {
        phase = PHASE_2;
        blankUnsolvedTiles();
        enableTiles(mLargeTiles);
        clearSelections();
        showMessage("Phase 2!");
    }

    private void blankUnsolvedTiles() {
        for (Tile largeTile : mLargeTiles) {
            if (largeTile.isAvailable()) {
                for (Tile tile : largeTile.getSubTiles()) {
                    tile.blank();
                }
            }
        }
    }

    private boolean nextMoveAvailable(int large, int small) {
        Tile tile = mSmallTiles[large][small];

        if (isPhase2()) {
            return large != mLastLarge && !tile.isBlank();
        }
        if (mLargeTiles[large].isAvailable() && mLastLarge == -1) {
            return true;
        }
        if (large != mLastLarge || tile.isSelected() || !tile.isAvailable() || tile.isBlank()) {
            return false;
        }
        int row = small / 3;
        int col = small - 3 * row;
        int lastRow = mLastSmall / 3;
        int lastCol = mLastSmall - 3 * lastRow;
        return small != mLastSmall && Math.abs(row - lastRow) <= 1
                && Math.abs(col - lastCol) <= 1;
    }
    
    private void makeMove(int large, int small) {
        Tile tile = mSmallTiles[large][small];
        word += tile.getValue();
        tile.setState(Tile.State.SELECTED); //refactor
        disableFromMove(large, small);
        mLastLarge = large;
        mLastSmall = small;
        updateAllTiles();
        updateWordDisplay();
    }

    private void disableFromMove(int large, int small) {
        if (isPhase2()) {
            disableFromMovePhase2(large, small);
            return;
        }
        if (mLastLarge != -1) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (i != large) {
                disableTiles(mSmallTiles[i]);
            }
        }
    }

    private void disableFromMovePhase2(int large, int small) {
        enableTiles();
        for (int i = 0; i < 9; i++) {
            Tile tile = mSmallTiles[large][i];
            if (i != small && !tile.isSelected()) {
                disableTiles(mSmallTiles[large][i]);
            }
        }
    }

    // UI update
    private void updateAllTiles() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateDrawableState();
            }
        }
    }

    private void updateWordDisplay() {
        ((ScroggleActivity)getActivity()).updateWord(word);
    }

    private void updatePointsDisplay() {
        ((ScroggleActivity)getActivity()).updatePoints();
    }

    public void submitWord() {
        if (word.length() == 0) {
            return;
        }
        int score = mGameInfo.getScore();
        if (mDBHelper.wordExists(word)) {
            wordsFound.add(word);
            int newScore = getPointsOfWord(word);
            score += newScore;
            mGameInfo.updateHighestScoreWord(word, newScore);
            playWordFoundSound();
            showMessage("Word Found!");
            if (isPhase2()) {
                removeSelectedSmallTiles();
            } else {
                removeUnselectedSmallTiles(mLastLarge);
                disableTiles(mLargeTiles[mLastLarge]);
            }
        } else {
            score = score > 5 ? score - 5 : 0;
            showMessage(word.toUpperCase() + " Is Not A Word!");
            playWrongWordSound();
        }
        mGameInfo.setScore(score);
        clearSelections();
        updatePointsDisplay();
    }

    public void clearSelections() {
        resetWord();
        updateWordDisplay();
        enableTiles();
        unselectAllTiles();
        updateAllTiles();
        resetLastMove();
    }

    private void resetWord() {
        word = "";
    }

    private int getPointsOfWord(String word) {
        char[] chs = word.toCharArray();
        int score = 0;
        for (char c : chs) {
            score += scores[c - 'a'];
        }
        if (word.length() == WORD_LENGTH) {
            score += BONUS;
        }
        return score;
    }

    private void unselectAllTiles() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                Tile smallTile = mSmallTiles[large][small];
                if (smallTile.isSelected()) {
                    mSmallTiles[large][small].enable();
                }
            }
        }
    }

    private void removeUnselectedSmallTiles(int large) {
        for (Tile tile : mSmallTiles[large]) {
            if (!tile.isSelected()) {
                tile.blank();
            }
        }
    }

    private void removeSelectedSmallTiles() {
        for (Tile[] tiles : mSmallTiles) {
            for (Tile tile : tiles) {
                if (tile.isSelected()) {
                    tile.blank();
                }
            }
        }
    }

    private void showMessage(String message) {
        Context context = getActivity();
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, message, duration);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 700);
    }

    private void playClickSound() {
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.letter_selected_sound);
        mMediaPlayer.start();
    }

    private void playWordFoundSound() {
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.word_found_sound);
        mMediaPlayer.start();
    }

    private void playWrongWordSound() {
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.word_wrong_sound);
        mMediaPlayer.setVolume(2.0f, 2.0f);
        mMediaPlayer.start();
    }

    public void toggleTilesVisibility() {
        for (Tile[] tiles : mSmallTiles) {
            for (Tile tile : tiles) {
                tile.toggleVisibility();
            }
        }
    }
}
