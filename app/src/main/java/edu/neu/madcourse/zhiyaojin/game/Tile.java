package edu.neu.madcourse.zhiyaojin.game;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.zhiyaojin.fragment.ScroggleFragment;

public class Tile {

    public enum State {
        UNSELECTED,
        SELECTED,
        DISABLED
    }

    private static final int LEVEL_UNSELECTED = 0;
    private static final int LEVEL_SELECTED = 1;
    private static final int LEVEL_DISABLED = 2;

    private final ScroggleFragment mScroggle;
    private State mState = State.UNSELECTED;
    private View mView;
    private String value = "";
    private Tile[] mSubTiles;

    public Tile(ScroggleFragment scroggle) {
        this.mScroggle = scroggle;
    }

    public Tile deepCopy() {
        Tile tile = new Tile(mScroggle);
        tile.setValue(value);
        if (getSubTiles() != null) {
            Tile newTiles[] = new Tile[9];
            Tile oldTiles[] = getSubTiles();
            for (int child = 0; child < 9; child++) {
                newTiles[child] = oldTiles[child].deepCopy();
            }
            tile.setSubTiles(newTiles);
        }
        return tile;
    }

    public State getState() {
        return mState;
    }

    public void setState(State mState) {
        this.mState = mState;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Tile[] getSubTiles() {
        return mSubTiles;
    }

    public void setSubTiles(Tile[] subTiles) {
        this.mSubTiles = subTiles;
    }

    public void disable() {
        mState = State.DISABLED;
    }

    public void enable() {
        mState = State.UNSELECTED;
    }

    public boolean isAvailable() {
        return mState != State.DISABLED;
    }

    public boolean isSelected() {
        return mState == State.SELECTED;
    }

    public void blank() {
        value = "";
    }

    public boolean isBlank() {
        return value.length() == 0;
    }

    public void updateDrawableState() {
        if (mView == null) return;
        int level = getLevel();
        if (mView.getBackground() != null) {
            mView.getBackground().setLevel(level);
        }
        if (mView instanceof Button) {
            ((Button) mView).setText(value);
            Drawable drawable = mView.getBackground();
            drawable.setLevel(level);
        }
    }

    private int getLevel() {
        int level = LEVEL_UNSELECTED;
        switch (mState) {
            case DISABLED:
                level = LEVEL_DISABLED;
                break;
            case SELECTED:
                level = LEVEL_SELECTED;
                break;
            case UNSELECTED:
                level = LEVEL_UNSELECTED;
        }
        return level;
    }

}

