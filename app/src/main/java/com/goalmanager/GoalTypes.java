package com.goalmanager;

import java.util.ArrayList;

class GoalTypes {
    private static final String SINGLE = "Single check";                //Single goal. It is either complete or it is not.
    private static final String SUB_SINGLE = "Multi check";             //Will have a series of checks, and when those are complete, the goal is complete.
    private static final String PERIODICAL = "Periodical";          //Will need to be done repetitively, indefinitely or up to a limit.

    static ArrayList<String> GetGoalTypes(){
        ArrayList<String> goals = new ArrayList<>();
        goals.add(SINGLE);
        goals.add(SUB_SINGLE);
        return goals;
    }
}
