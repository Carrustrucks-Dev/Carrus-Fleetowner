package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.carrus.fleetowner.utils.SessionManager;

/**
 * Created by Sunny on 12/3/15.
 */
public class SplashActivity extends BaseActivity {

    private SessionManager mSessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Force Crash
//        int i=1/0;

        int density= getResources().getDisplayMetrics().densityDpi;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
                break;
        }


        mSessionManager = new SessionManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSessionManager.isLoggedIn()) {
                    startActivityForResult(new Intent(SplashActivity.this, MainActivity.class), 500);
                } else {
                    startActivityForResult(new Intent(SplashActivity.this, LoginActivity.class), 500);
                }
                finish();
            }
        }, 2000);
    }
}
