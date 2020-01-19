package com.goalmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkRegulator extends Worker {

    private Context context;

    public WorkRegulator(@NonNull Context context, @NonNull WorkerParameters params){
        super(context,params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork(){
        ReminderManager reminderManager = new ReminderManager(context);
        reminderManager.refreshAll();
        return Result.success();
    }

}
