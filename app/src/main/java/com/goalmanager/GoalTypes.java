package com.goalmanager;

import java.util.ArrayList;

class GoalTypes {
    public static final String SINGLE = "Single check";                 //Single goal. It is either complete or it is not.
    private static final String SUB_SINGLE = "Multi check";             //Will have a series of checks, and when those are complete, the goal is complete.
    private static final String PERIODICAL = "Periodical";              //Will need to be done repetitively, indefinitely or up to a limit.
    private static final String PERMANENT = "Permanent";                //Will stay as a permanent notification until the user completes it.

    static ArrayList<String> GetGoalTypes(){
        //Returns an array of all the types a goal can have.

        ArrayList<String> types = new ArrayList<>();
        types.add(SINGLE);
        types.add(SUB_SINGLE);
        return types;
    }
}
