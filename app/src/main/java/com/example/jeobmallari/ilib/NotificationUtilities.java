package com.example.jeobmallari.ilib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jeob Mallari on 5/9/2017.
 */

public class NotificationUtilities {
    private static final int NOTIFICATION_INTERVAL_MINUTES = 1260;
    private static final int NOTIFICATION_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(NOTIFICATION_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = NOTIFICATION_INTERVAL_SECONDS;

    private static final String NOTIFICATION_JOB_TAG = "notification_tag";

    private static boolean sInitialized;

    synchronized public static void fireNotification(@NonNull final Context context){
        if(sInitialized){
            return;
        }
        else {
            Driver driver = new GooglePlayDriver(context);
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

            Job notificationJob = dispatcher.newJobBuilder()
                    .setService(FirebaseJobService.class)
                    .setTag(NOTIFICATION_JOB_TAG)
                    .setLifetime(Lifetime.FOREVER)
                    // .setConstraints(Constraint.ON_ANY_NETWORK)   // notif will fire only when connection is available
                    // .setRecurring(true)      // notif will re-fire itself
                    .setTrigger(Trigger.executionWindow(NOTIFICATION_INTERVAL_SECONDS,
                            NOTIFICATION_INTERVAL_SECONDS+10)
                    )
                    .setReplaceCurrent(true)
                    .build();

            dispatcher.schedule(notificationJob);
            Log.e(NOTIFICATION_JOB_TAG, "Job is dispatched");
            sInitialized = true;
        }
    }
}
