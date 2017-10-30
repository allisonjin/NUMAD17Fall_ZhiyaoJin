package edu.neu.madcourse.zhiyaojin.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.dao.GameDao;
import edu.neu.madcourse.zhiyaojin.entity.GameInfo;
import edu.neu.madcourse.zhiyaojin.listener.OnGetDataListener;

public class LeaderBoardFragment extends Fragment {

    private GameDao mGameDao;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameDao = new GameDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_leader_board, container, false);

        final List<GameInfo> highestGames = new ArrayList<>();
        mGameDao.getHighestGames(new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                        Log.i("leaderboard", "test");
                        highestGames.add(0, gameSnapshot.getValue(GameInfo.class));
                    }
                }
                ListView listView = rootView.findViewById(R.id.leader_board);
                listView.setAdapter(new LeaderBoardAdapter(getActivity(), highestGames));
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private class LeaderBoardAdapter extends ArrayAdapter {

        private final Activity context;
        private final List<GameInfo> highestGames;

        public LeaderBoardAdapter(Activity context, List<GameInfo> highestGames) {
            super(context, R.layout.leader_board_single, highestGames);
            this.context = context;
            this.highestGames = highestGames;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.leader_board_single, null, true);
            GameInfo game = highestGames.get(position);
            TextView usernameTextView = rowView.findViewById(R.id.leader_username);
            TextView scoreTextView = rowView.findViewById(R.id.leader_score);
            TextView wordTextView = rowView.findViewById(R.id.leader_highest_word);
            TextView dateTextView = rowView.findViewById(R.id.leader_date);

            usernameTextView.setText(game.getUsername());
            scoreTextView.setText(String.valueOf(game.getScore()));
            wordTextView.setText(game.getHighestScoreWord() + " (" + game.getHighestScore() + ")");
            dateTextView.setText(game.getDate());
            return rowView;
        }
    }

}
