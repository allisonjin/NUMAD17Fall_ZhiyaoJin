package edu.neu.madcourse.zhiyaojin.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.R;

public class DisplayAboutActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_about);
        getSupportActionBar().setTitle(R.string.about_title);
        TextView imei = (TextView)findViewById(R.id.about_imei);
        imei.setText(getImeiId());
    }

    private String getImeiId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = telephonyManager.getDeviceId();
        if (uuid != null) {
            return uuid;
        }
        return "This phone has no device id!";
    }
}
