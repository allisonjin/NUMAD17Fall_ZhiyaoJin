package edu.neu.madcourse.zhiyaojin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        Intent intent = new Intent(this, DisplayAboutActivity.class);
        startActivity(intent);
    }

    public void generateError(View view) {
        throw new RuntimeException("This is a crash");
    }

}
