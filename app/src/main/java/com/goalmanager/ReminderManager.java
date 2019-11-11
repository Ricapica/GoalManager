package com.goalmanager;
/*
This class exists to manage and keep all the other workers in check.
 */


import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

class ReminderManager {
    private ArrayList<Goal> goalsToRemind;
    private Context context;

    void refreshAll(){
        WorkManager.getInstance(context).cancelAllWork();
        for(Goal g:goalsToRemind){
            switch (g.reminderType){
                case GoalReminderType.DAILY:
                    Calendar cal =Calendar.getInstance();
                    long now = cal.getTimeInMillis();

                    cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(g.timeData.substring(0,2)));
                    cal.set(Calendar.MINUTE,Integer.parseInt(g.timeData.substring(2,4)));
                    cal.set(Calendar.SECOND,0);
                    long targetTime=cal.getTimeInMillis();
                    if(targetTime-now<0)
                    {
                        //If the time has passed already, the time will be the next day.
                        targetTime+=1000*60*60*24;
                    }
                    PeriodicWorkRequest setReminder = new PeriodicWorkRequest.Builder(ReminderWorker.class,24, TimeUnit.HOURS)
                            .setInitialDelay(targetTime-now,TimeUnit.MILLISECONDS)
                            .addTag(g.title)
                            .build();
                    WorkManager.getInstance(context).enqueueUniquePeriodicWork(g.title, ExistingPeriodicWorkPolicy.REPLACE,setReminder);
                    break;
                case GoalReminderType.WEEKLY:

                default:
                    break;
            }
        }
    }










    ReminderManager(Context context){
        this.context = context;
        goalsToRemind = new ArrayList<>();
    }

    void addGoalToRemind(Goal g){
        goalsToRemind.remove(g);
        goalsToRemind.add(g);

        Calendar cal =Calendar.getInstance();
        long now = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(g.timeData.substring(0,2)));
        cal.set(Calendar.MINUTE,Integer.parseInt(g.timeData.substring(2,4)));
        cal.set(Calendar.SECOND,0);
        long targetTime=cal.getTimeInMillis();
        if(targetTime-now<0)
        {
            //If the time has passed already, the time will be the next day.
            targetTime+=1000*60*60*24;
        }
        PeriodicWorkRequest setReminder = new PeriodicWorkRequest.Builder(ReminderWorker.class,24, TimeUnit.HOURS)
                .setInitialDelay(targetTime-now,TimeUnit.MILLISECONDS)
                .addTag(g.title)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(g.title, ExistingPeriodicWorkPolicy.REPLACE,setReminder);
    }

    void removeGoalFromRemind(Goal goal){
        goalsToRemind.remove(goal);
        WorkManager.getInstance(context).cancelUniqueWork(goal.title);
    }

    void setGoalsList(ArrayList<Goal> goalsList){
        goalsToRemind.clear();
        for(Goal g:goalsList) {
            if(g.hasReminders) {
                goalsToRemind.add(g);
            }
            else{

            }
        }

    }
}
