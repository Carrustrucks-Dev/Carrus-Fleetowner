package com.carrus.fleetowner.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.carrus.fleetowner.BuildConfig;
import com.carrus.fleetowner.R;
import com.carrus.fleetowner.SplashActivity;

import org.json.JSONObject;


public class GcmMessageHandler extends IntentService {
    private static final int MESSAGE_NOTIFICATION_ID = 435345;

    //Device Token:  APA91bG8ySyWfcLn1txW873blkcAWJMbLsBcN4Xy7t03NiPEkkAPVYWgYEOu4O-tEmi_7YWj3Njc-soMYuaY3nNjleBunExvow8BHhYDtd457Zumy-XrU2mylYXJPTevhcHSDVNtQp6k
    //API Key:  AIzaSyAlmqhW0x5MOLv6mOOS3QMrrijDebmIIPE

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    public static void ClearNotification(Context c) {
        Log.v("ClearNotification", "ClearNotification");

        NotificationManager notifManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve data extras from push notification
        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // Keys in the data are shown as extras
        if(BuildConfig.DEBUG)
        Log.i("Complete payload", intent.getExtras().toString());
        try {
//            String msg = extras.getString("message");
//            sendNotification(extras.getString("message").toString(), extras.getString("gcm.notification.title").toString());
            JSONObject myObject = new JSONObject(extras.getString("flag"));
//            if (myObject.has("bookingStatus")) {
//
//            } else
            sendNotification(extras.getString("message").replaceAll("_", " "), extras.getString("brand_name"), myObject.getString("bookingId"));

        } catch (Exception e) {
            sendNotification("", "Carrus Shipper", "");
        }

        // Notify receiver the intent is completed
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, String title, String id) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("id", id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon()).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg)).setAutoCancel(true)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);
        mBuilder.setVibrate(new long[]{1000, 1000});
        mBuilder.setLights(Color.BLUE, 300, 300);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

//    private void sendRatingNotification(String msg, String title, String id) {
//        mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        final Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.setAction(Intent.ACTION_MAIN);
//        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        notificationIntent.putExtra("fromNotification", true);
//        notificationIntent.putExtra("id", id);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(getNotificationIcon()).setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                        getNotificationIcon()))
//                        .setContentTitle(title)
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg)).setAutoCancel(true)
//                        .setContentText(msg);
//
//        mBuilder.setContentIntent(contentIntent);
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        mBuilder.setSound(uri);
//        mBuilder.setVibrate(new long[]{1000, 1000});
//        mBuilder.setLights(Color.BLUE, 300, 300);
//        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
//    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.mipmap.notification_icon : R.mipmap.ic_launcher;
    }


}