package edu.neu.madcourse.zhiyaojin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.game.SendMessageAsyncTask;

public class CongratsDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_congrats_dialog);
        Intent intent = getIntent();
        final String gameId = intent.getExtras().getString("gameId");
        final String body = intent.getExtras().getString("body");

        TextView textView = (TextView)findViewById(R.id.congrats_body);
        textView.setText(body);

        Button congratsBtn = (Button)findViewById(R.id.congrats_btn);
        if (gameId == null) {
            congratsBtn.setText(R.string.btn_ok);
        }
        congratsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameId != null) {
                    new SendMessageAsyncTask(gameId, null, "Congratulations!", "Congratulations!").execute();
                }
                finishActivity();
            }
        });


    }

    private void finishActivity() {
        this.finish();
    }

}
