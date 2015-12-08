package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Sunny on 12/3/15.
 */
public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
