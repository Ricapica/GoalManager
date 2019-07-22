package com.goalmanager;

import java.util.ArrayList;

public class GoalReminderType {
    static final String DAILY = "Daily";              //Every day, same time.
    static final String WEEKLY = "Weekly";              //Every week, same day and same time.
    static final String MONTHLY = "Monthly";              //Every month, same date.
    static final String PERIODICAL = "Periodical";              //Will need to be done repetitively, indefinitely or up to a limit.
    static final String PERMANENT = "Permanent";                //Will stay as a permanent notification until the user completes it.

    static ArrayList<String> GetReminderTypes(){
        //Returns an array of all the types a goal reminder can have.

        ArrayList<String> types = new ArrayList<>();

        types.add(DAILY);
        types.add(WEEKLY);
        types.add(MONTHLY);
        types.add(PERIODICAL);
        types.add(PERMANENT);
        return types;
    }
}
