package com.goalmanager;

import java.util.ArrayList;

class GoalTypes {
    static final String SINGLE = "Single check";                 //Single goal. It is either complete or it is not.
    static final String SUB_SINGLE = "Multi check";             //Will have a series of checks, and when those are complete, the goal is complete.

    static ArrayList<String> GetGoalTypes(){
        //Returns an array of all the types a goal can have.

        ArrayList<String> types = new ArrayList<>();
        types.add(SINGLE);
        types.add(SUB_SINGLE);
        return types;
    }
}
