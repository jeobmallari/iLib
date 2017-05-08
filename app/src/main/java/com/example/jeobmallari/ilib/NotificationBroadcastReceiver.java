package com.example.jeobmallari.ilib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Jeob Mallari on 5/7/2017.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    String title;
    @Override
    public void onReceive(Context context, Intent intent) {
        //title = intent.getStringExtra("title");
        Intent callBackIntent = new Intent(context, Home.class);
        callBackIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        callBackIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, callBackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
                .setContentTitle("You have a reserved material to claim!")
                .setContentText(title)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_milib1))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent);

        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(1, builder.build());
    }
}
