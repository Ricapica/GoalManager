package com.goalmanager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class JobManager extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("Rica", "IT IS TIME TO START THE JOB");
        jobFinished(params,false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
