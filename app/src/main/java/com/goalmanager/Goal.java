package com.goalmanager;

import androidx.annotation.NonNull;

public class Goal {

    String title;
    String subtitle;
    String category;

    Goal(String title, String subtitle){
        this.title = title;
        this.subtitle = subtitle;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: "+title+
                " Description: "+subtitle+
                " Category: "+category;
    }
}
