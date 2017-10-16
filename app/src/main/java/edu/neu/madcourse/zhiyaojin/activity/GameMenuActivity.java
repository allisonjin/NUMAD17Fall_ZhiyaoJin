package edu.neu.madcourse.zhiyaojin.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.fragment.AcknowledgmentDialogFragment;

public class GameMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        getSupportActionBar().setTitle(R.string.game_title);
        Button acknowledgmentBtn = (Button)findViewById(R.id.game_acknowledgment);
        acknowledgmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = new StringBuilder()
                        .append("\nSounds:\n")
                        .append("www.dl-sounds.com\n")
                        .append("freesound.org\n")
                        .append("\nIcons:\n")
                        .append("icons8.com")
                        .toString();

                FragmentManager fm = getSupportFragmentManager();
                AcknowledgmentDialogFragment dialog = new AcknowledgmentDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("source", source);
                dialog.setArguments(bundle);
                dialog.setRetainInstance(true);
                dialog.show(fm, "acknowledgment");
            }
        });
    }
}
