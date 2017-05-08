package com.example.jeobmallari.ilib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Created by Jeob Mallari on 5/8/2017.
 */

public class ScheduleClient {

    private ScheduleService mBoundService;
    private Context mContext;
    private boolean mIsBound;

    public ScheduleClient(Context context) {
        mContext = context;
    }

    public void doBindService() {
        // Establish a connection with our service
        Intent intent = new Intent(mContext, ScheduleService.class);
        //mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mContext.startService(intent);
        mIsBound = true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((ScheduleService.ServiceBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    public void setAlarmForNotification(Calendar c){
        mBoundService.setAlarm(c);
    }

    public void doUnbindService() {
        if (mIsBound) {
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }
}