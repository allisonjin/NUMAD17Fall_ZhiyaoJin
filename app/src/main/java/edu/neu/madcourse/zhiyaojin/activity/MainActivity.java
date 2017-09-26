package edu.neu.madcourse.zhiyaojin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.BuildConfig;
import edu.neu.madcourse.zhiyaojin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.main_title);
        TextView versionCode = (TextView)findViewById(R.id.version_code);
        versionCode.setText(String.valueOf(BuildConfig.VERSION_CODE));
        TextView versionName = (TextView)findViewById(R.id.version_name);
        versionName.setText(BuildConfig.VERSION_NAME);
    }

    public void displayAbout(View view) {
        startActivity(new Intent(this, DisplayAboutActivity.class));
    }

    public void generateError(View view) {
        throw new RuntimeException("This is a crash");
    }

    public void displayDictionary(View view) {
        startActivity(new Intent(this, DictionaryActivity.class));
    }
}
