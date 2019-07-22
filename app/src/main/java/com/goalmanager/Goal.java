package com.goalmanager;

import android.util.Log;

import androidx.annotation.NonNull;

/*
    The Goal Class represents a User's single goal.
    The Goal has several attributes that user can customize.

    Title: The name of the goal.
    Subtitle: The description of the goal.
    Category: A set this goal belongs to. This helps with joining goals together.

 */
public class Goal {
    public int id;
    public String title;
    public String subtitle;
    public String category;
    public String goalType;
    public boolean hasReminders;
    public String reminderData;
    public String timeData;
    public boolean complete;

    Goal(String title, String subtitle){


        this.title = title;
        this.subtitle = subtitle;


        goalType = GoalTypes.SINGLE;
        hasReminders = false;
        reminderData = "0";     //0 for off.
        timeData = "";
        complete = false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: "+title+
                " Description: "+subtitle+
                " Category: "+category+
                " Goal Type: "+goalType+
                " Reminders: "+hasReminders+
                " ReminderData: "+reminderData+
                " TimeData: "+timeData+
                " Complete: "+complete;
    }

    public boolean IsValid(){
        Log.e("Rica",toString());
        return title!=null && subtitle!=null && category!=null && goalType!=null;
    }
    public boolean SupportsReminders(){
        return goalType.equals(GoalTypes.SINGLE);
    }
}
