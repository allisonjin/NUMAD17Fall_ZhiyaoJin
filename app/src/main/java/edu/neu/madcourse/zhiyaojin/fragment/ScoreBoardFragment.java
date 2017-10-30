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
import edu.neu.madcourse.zhiyaojin.dao.UserDao;
import edu.neu.madcourse.zhiyaojin.entity.GameInfo;
import edu.neu.madcourse.zhiyaojin.listener.OnGetDataListener;

public class ScoreBoardFragment extends Fragment {

    private UserDao mUserDao;

    public ScoreBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDao = new UserDao(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_score_board, container, false);

        final List<GameInfo> highestGames = new ArrayList<>();
        mUserDao.getHighestUserGames(new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                        highestGames.add(0, gameSnapshot.getValue(GameInfo.class));
                    }
                }

                ListView listView = rootView.findViewById(R.id.score_board);
                listView.setAdapter(new ScoreBoardAdapter(getActivity(), highestGames));
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private class ScoreBoardAdapter extends ArrayAdapter {

        private final Activity context;
        private final List<GameInfo> highestGames;

        public ScoreBoardAdapter(Activity context, List<GameInfo> highestGames) {
            super(context, R.layout.score_board_single, highestGames);
            this.context = context;
            this.highestGames = highestGames;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.score_board_single, null, true);
            GameInfo game = highestGames.get(position);
            TextView scoreTextView = rowView.findViewById(R.id.score_score);
            TextView wordTextView = rowView.findViewById(R.id.score_highest_word);
            TextView dateTextView = rowView.findViewById(R.id.score_date);

            scoreTextView.setText(String.valueOf(game.getScore()));
            wordTextView.setText(game.getHighestScoreWord() + " (" + game.getHighestScore() + ")");
            dateTextView.setText(game.getDate());
            return rowView;
        }
    }

}
