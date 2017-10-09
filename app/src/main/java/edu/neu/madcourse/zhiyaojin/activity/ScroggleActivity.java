package edu.neu.madcourse.zhiyaojin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.fragment.ScroggleFragment;

public class ScroggleActivity extends AppCompatActivity {

    private ScroggleFragment mScroggleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroggle);
        mScroggleFragment = (ScroggleFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_scroggle);
    }

    public void submitWord(View view) {
        mScroggleFragment.submitWord();
    }
}
