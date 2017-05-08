package com.example.jeobmallari.ilib;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static com.example.jeobmallari.ilib.BookDetail.context;
import static com.example.jeobmallari.ilib.R.drawable.ic_notifications_black_24dp;

/**
 * Created by Jeob Mallari on 5/8/2017.
 */

public class NotifyService extends Service {

    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }
    private static final int NOTIFICATION = 123;
    public static final String INTENT_NOTIFY = "INTENT_NOTIFY";
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new ServiceBinder();

    private void showNotification() {
        Intent callBackIntent = new Intent(context, Home.class);
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

        mNM.notify(NOTIFICATION, builder.build());

        stopSelf();
    }

}
