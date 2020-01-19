package com.goalmanager;
/*
This class exists to manage and keep all the other workers in check.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

class ReminderManager {
    private ArrayList<Goal> goalsToRemind;
    private Context context;

    ReminderManager(Context context){
        this.context = context;
        goalsToRemind = new ArrayList<>();
    }

    void setBaseReminder(){
        /*
        This is the base Worker for the app.
        It's job is to keep the other workers in check, disallowing them from drifting due to android.
        It will call refreshAll which should reset all jobs to their correct times.
        */

        Calendar cal =Calendar.getInstance();
        long now = cal.getTimeInMillis();
        cal.set(Calendar.MINUTE,23);
        cal.set(Calendar.SECOND,0);
        long targetTime = cal.getTimeInMillis();
        if(targetTime-now<0){
            targetTime += 1000*60*60*24;
        }
        PeriodicWorkRequest basePeriodicWork = new PeriodicWorkRequest.Builder(WorkRegulator.class,4,TimeUnit.HOURS)
                .setInitialDelay(targetTime-now, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("WORKREGULATOR",ExistingPeriodicWorkPolicy.KEEP, basePeriodicWork);
    }

    void refreshAll(){
        //Remove all current work.
        WorkManager.getInstance(context).cancelAllWork();

        //Set the base reminder again.
        setBaseReminder();

        //Get the viable goals that have a reminder set.
        getGoalsList();


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

    private void getGoalsList(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        String goalString = sharedPreferences.getString("goals","");

        ArrayList<Goal> goalsList = Utils.LoadFromJSON(goalString);

        goalsToRemind.clear();
        for(Goal g:goalsList) {
            if(g.hasReminders) {
                Log.e("getGoalsList","Viable goal: "+g.title);
                goalsToRemind.add(g);
            }
        }

    }
}
