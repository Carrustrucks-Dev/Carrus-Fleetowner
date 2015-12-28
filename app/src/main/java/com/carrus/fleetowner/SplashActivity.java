package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
