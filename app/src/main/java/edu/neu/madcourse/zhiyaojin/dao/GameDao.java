package edu.neu.madcourse.zhiyaojin.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import edu.neu.madcourse.zhiyaojin.entity.GameInfo;
import edu.neu.madcourse.zhiyaojin.listener.OnGetDataListener;

public class GameDao extends BaseDao {

    private final static String GAME_PATH = "games";
    private final static String SCORE_PATH = "score";
    private final static int HIGHEST_NUM = 10;

    private DatabaseReference gameRef;

    public GameDao() {
        super();
        gameRef = getRootRef().child(GAME_PATH);
    }

    public String addGame(GameInfo gameInfo) {
        String gameId = gameRef.push().getKey();
        gameRef.child(gameId).setValue(gameInfo);
        return gameId;
    }

    public void getHighestGames(OnGetDataListener listener) {
        Query queryHighestGames = gameRef.orderByChild(SCORE_PATH).limitToLast(HIGHEST_NUM);
        queryHighestGames.keepSynced(true);
        readData(queryHighestGames, listener);
    }

    public void getHighestGame(OnGetDataListener listener) {
        Query queryHighestGame = gameRef.orderByChild(SCORE_PATH).limitToLast(1);
        queryHighestGame.keepSynced(true);
        readData(queryHighestGame, listener);
    }

}
