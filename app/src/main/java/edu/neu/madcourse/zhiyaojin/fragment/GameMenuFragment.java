package edu.neu.madcourse.zhiyaojin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.activity.ScroggleActivity;

public class GameMenuFragment extends Fragment {

    public GameMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_menu, container, false);
        Button newGameButton = rootView.findViewById(R.id.new_game_btn);
        Button acknowledgmentButton = rootView.findViewById(R.id.game_acknowledgment);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScroggleActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

}
