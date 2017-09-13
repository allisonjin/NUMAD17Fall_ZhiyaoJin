package edu.neu.madcourse.zhiyaojin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.util.UUID;

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

    @TargetApi(26)
    private String getImeiId() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_DENIED) {
            String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getImei();
        if (imei != null) {
            return imei;
        }
        String meid = telephonyManager.getMeid();
        if (meid != null) {
            return meid;
        }
        return "This phone has no device id!";
    }
}
