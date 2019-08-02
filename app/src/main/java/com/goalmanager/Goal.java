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
    public String title;
    public String subtitle;
    String category;
    String goalType;
    boolean hasReminders;
    String reminderType;
    String reminderData;
    String timeData;
    public boolean complete;

    Goal(String title, String subtitle){


        this.title = title;
        this.subtitle = subtitle;

        //Default goal settings initialization.

        goalType = GoalTypes.SINGLE;
        hasReminders = false;
        reminderType = GoalReminderType.DAILY;
        reminderData = "0";     //0 for off.
        timeData = "";
        complete = false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: " + title +
                " Description: " + subtitle +
                " Category: " + category +
                " Goal Type: " + goalType +
                " Reminders: " + hasReminders +
                " Reminder Type: " + reminderType +
                " ReminderData: " + reminderData +
                " TimeData: " + timeData +
                " Complete: " + complete;
    }

    boolean IsValid(){
        Log.e("Rica",toString());
        return title!=null && subtitle!=null && category!=null && goalType!=null;
    }
    boolean SupportsReminders(){
        return goalType.equals(GoalTypes.SINGLE) || goalType.equals(GoalTypes.SUB_SINGLE);
    }
}
