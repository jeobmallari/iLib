package com.example.jeobmallari.ilib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Jeob Mallari on 5/9/2017.
 */

public class FireNotificationsTask {
    public static final String ACTION_FIRE_NOTIFICATION = "fire-notification";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";

    private static NotificationManager mNotifsManager;

    public static void executeTask(Context context, String action){
        if(action.equals(ACTION_FIRE_NOTIFICATION)){
            fireNotification(context);
        }
        else if(action.equals(ACTION_DISMISS_NOTIFICATION)){
            dismissAllNotifs(context);
        }
    }

    private static void dismissAllNotifs(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static void fireNotification(Context context) {
        Intent callBackIntent = new Intent(context, LoginActivity.class);
        callBackIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        callBackIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, callBackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("You have a reserved material to claim!")
                .setContentText(BookDetail.bookTitle)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(), R.mipmap.ic_launcher_milib1))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent);

        mNotifsManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifsManager.notify(1, builder.build());
    }
}
