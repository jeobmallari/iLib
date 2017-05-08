package com.example.jeobmallari.ilib;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Jeob Mallari on 5/9/2017.
 */

public class FirebaseJobService extends JobService {
    private AsyncTask mBackgroundTask;
    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = FirebaseJobService.this;
                FireNotificationsTask.executeTask(context, FireNotificationsTask.ACTION_FIRE_NOTIFICATION);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mBackgroundTask == null) mBackgroundTask.cancel(true);
        return false;
    }
}
