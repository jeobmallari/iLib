package com.example.jeobmallari.ilib;

import android.app.Service;
import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Jeob Mallari on 5/8/2017.
 */

public class ScheduleService extends Service {
    public class ServiceBinder extends Binder {
        ScheduleService getService() {
            return ScheduleService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new ServiceBinder();

    public void setAlarm(Calendar c) {
        new AlarmTask(this, c).run();
    }
}
