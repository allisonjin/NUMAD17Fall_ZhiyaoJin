package edu.neu.madcourse.zhiyaojin.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import org.apache.commons.io.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import edu.neu.madcourse.zhiyaojin.Constants;
import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.activity.HighScoresActivity;
import edu.neu.madcourse.zhiyaojin.activity.ScroggleActivity;
import edu.neu.madcourse.zhiyaojin.dao.UserDao;
import edu.neu.madcourse.zhiyaojin.game.Tile;
import edu.neu.madcourse.zhiyaojin.listener.OnGetDataListener;

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
        Button editUsernameButton = rootView.findViewById(R.id.edit_username_btn);
        Button highScoresButton = rootView.findViewById(R.id.high_scores_btn);
        Button acknowledgmentButton = rootView.findViewById(R.id.game_acknowledgment);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScroggleActivity.class);
                getActivity().startActivity(intent);
            }
        });


        final UserDao userDao = new UserDao(getActivity());

        editUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());

                userDao.readLastUsername(new OnGetDataListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.getValue() != null ?
                                dataSnapshot.getValue().toString() : "Anonymous";
                        editText.setText(username);
                    }

                    @Override
                    public void onFailed(DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.edit_username_title);
                builder.setView(editText);

                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = editText.getText().toString();
                        userDao.setLastUsername(username);
                    }
                });

                builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HighScoresActivity.class);
                getActivity().startActivity(intent);
            }
        });

        acknowledgmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = new StringBuilder()
                        .append("\nSounds:\n")
                        .append("www.dl-sounds.com\n")
                        .append("freesound.org\n")
                        .append("\nIcons:\n")
                        .append("icons8.com")
                        .toString();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                AcknowledgmentDialogFragment dialog = new AcknowledgmentDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("source", source);
                dialog.setArguments(bundle);
                dialog.setRetainInstance(true);
                dialog.show(fm, "acknowledgment");
            }
        });

        return rootView;
    }

}
