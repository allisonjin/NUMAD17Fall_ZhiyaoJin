package edu.neu.madcourse.zhiyaojin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.dictionary.DictionaryDBHelper;
import edu.neu.madcourse.zhiyaojin.game.BoggleBoardGenerator;
import edu.neu.madcourse.zhiyaojin.game.Tile;

public class ScroggleFragment extends Fragment {

    private static int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    private static int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};
    private final static int BOARD_SIZE = 3;

    private static String[] testWord = {"c", "o", "m", "t", "u", "p", "e", "r", "s"};
    private final DictionaryDBHelper dbHelper;
    private final BoggleBoardGenerator boardGenerator;

    private Tile mEntireBoard = new Tile(this);
    private Tile[] mLargeTiles = new Tile[9];
    private Tile[][] mSmallTiles = new Tile[9][9];
    private String word = "";
    private int mLastLarge;
    private int mLastSmall;

    public ScroggleFragment() {
        dbHelper = new DictionaryDBHelper(getContext());
        boardGenerator = new BoggleBoardGenerator(dbHelper, BOARD_SIZE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
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

    private void disableTiles(Tile... tiles) {
        for (Tile t : tiles) {
            t.disable();
        }
    }

    private void disableLargeTile(Tile tile) {
        tile.disable();
        disableTiles(tile.getSubTiles());
    }

    private void enableTiles() {
        for (Tile largeTile : mLargeTiles) {
            if (largeTile.isAvailable()) {
                for (Tile smallTile : largeTile.getSubTiles()) {
                    smallTile.enable();
                }
            }
        }
    }

    private void resetLastMove() {
        mLastSmall = -1;
        mLastLarge = -1;
    }

    public void initGame() {
        mEntireBoard = new Tile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            char[] board = boardGenerator.getRandomBoard();
            for (int small = 0; small < 9; small++) {
                Tile tile = new Tile(this);
                tile.setValue(String.valueOf(board[small]));
                mSmallTiles[large][small] = tile;
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);
        resetLastMove();
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for (int small = 0; small < 9; small++) {
                Button inner = (Button) outer.findViewById
                        (mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final Tile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);
                // ...
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (nextMoveAvailable(fSmall) && smallTile.isAvailable()) {
//                            Log.i("click", "" + fLarge + " " + fSmall);
                            makeMove(fLarge, fSmall);
                        }
                    }
                });
                // ...
            }
        }
    }

    private boolean nextMoveAvailable(int small) {
        if (mLastLarge == -1) {
            return true;
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
        tile.setState(Tile.State.SELECTED);
        disableFromMove(large);
        mLastLarge = large;
        mLastSmall = small;
        updateAllTiles();
    }

    private void disableFromMove(int large) {
        if (mLastLarge != -1) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (i != large) {
                disableTiles(mSmallTiles[i]);
            }
        }
    }

    private void updateAllTiles() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateDrawableState();
            }
        }
    }

    public void submitWord() {
        if (word.length() == 0) {
            return;
        }
        if (dbHelper.wordExists(word)) {
            removeUnselectedTiles();
            disableLargeTile(mLargeTiles[mLastLarge]);
        } else {
            // penalize
        }
        enableTiles();
        resetLastMove();
        updateAllTiles();
        word = "";
    }

    private void removeUnselectedTiles() {
        for (Tile tile : mSmallTiles[mLastLarge]) {
            if (!tile.isSelected()) {
                tile.blank();
            }
        }
    }

}
