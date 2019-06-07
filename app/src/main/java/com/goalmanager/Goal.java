package com.goalmanager;

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
//    public String category;

    Goal(String title, String subtitle){
        this.title = title;
        this.subtitle = subtitle;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: "+title+
                " Description: "+subtitle;
    }
}
