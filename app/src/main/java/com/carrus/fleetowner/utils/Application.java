package com.carrus.fleetowner.utils;


import com.carrus.fleetowner.R;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Muddassir on 6/10/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Helvetica/Helvetica.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
