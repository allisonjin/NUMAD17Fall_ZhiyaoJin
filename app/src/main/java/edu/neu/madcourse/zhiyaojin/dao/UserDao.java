package edu.neu.madcourse.zhiyaojin.dao;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.zhiyaojin.entity.GameInfo;
import edu.neu.madcourse.zhiyaojin.listener.OnGetDataListener;
import edu.neu.madcourse.zhiyaojin.utils.Installation;

public class UserDao extends BaseDao {

    private final static String USERS_PATH = "users";
    private final static String LAST_USERNAME_PATH = "lastUsername";
    private final static String GAMES_PATH = "games";
    private final static String SCORE_PATH = "score";
    private final static int HIGHEST_NUM = 10;

    private DatabaseReference userRef;

    public UserDao(Context context) {
        super();
        String userId = Installation.id(context);
        userRef = getRootRef().child(USERS_PATH).child(userId);
        userRef.keepSynced(true);
    }

    public void readLastUsername(final OnGetDataListener listener) {
        readData(userRef.child(LAST_USERNAME_PATH), listener);
    }

    public void setLastUsername(String username) {
        userRef.child(LAST_USERNAME_PATH).setValue(username);
    }

    public void addUserGame(String gameId, GameInfo gameInfo) {
        userRef.child(GAMES_PATH).child(gameId).setValue(gameInfo);
    }

    public void getHighestUserGames(OnGetDataListener listener) {
        Query queryHighestUserGame = userRef.child(GAMES_PATH)
                .orderByChild(SCORE_PATH)
                .limitToLast(HIGHEST_NUM);
        readData(queryHighestUserGame, listener);
    }


}
