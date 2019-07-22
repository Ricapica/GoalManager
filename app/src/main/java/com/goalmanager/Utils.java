package com.goalmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class Utils {
    static String SaveAsJSON(ArrayList<Goal> goals){
        String start="{\"Goals\":[";
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        for(Goal goal:goals){

            if(goal == null || !goal.IsValid()){
                continue;
            }

            sb.append("{"); //Opening brace for a new object goal

            sb.append("\"Title\":\"").append(goal.title.replace("\"","'")).append("\",");
            sb.append("\"Subtitle\":\"").append(goal.subtitle.replace("\"","'")).append("\",");
            sb.append("\"Category\":\"").append(goal.category.replace("\"","'")).append("\",");
            sb.append("\"Type\":\"").append(goal.goalType).append("\",");
            sb.append("\"Reminders\":\"").append(goal.hasReminders?"T":"F").append("\",");
            sb.append("\"ReminderType\":\"").append(goal.reminderType).append("\",");
            sb.append("\"ReminderData\":\"").append(goal.reminderData).append("\",");
            sb.append("\"TimeData\":\"").append(goal.timeData).append("\",");
            sb.append("\"Complete\":\"").append(goal.complete?"T":"F").append("\",");

            sb.deleteCharAt(sb.length()-1); //Delete trailing comma
            sb.append("},"); //Closing bracket--End of Object
        }

        if(sb.charAt(sb.length()-1)==',') {
            sb.deleteCharAt(sb.length() - 1); //Delete trailing comma.
        }
        sb.append("]}"); //Closing bracket--End of JSON
        return sb.toString();
    }

    static ArrayList<Goal> LoadFromJSON(String json){
        ArrayList<Goal> list = new ArrayList<>();
        try {

            Log.e("Rica", "Loading");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray goalArray = (org.json.JSONArray) jsonObject.get("Goals");
            for(int i=0;i<goalArray.length();i++){
                String goal = ((org.json.JSONArray)jsonObject.get("Goals")).getString(i);
                Log.e("Rica",goal);
                list.add(GoalFromJson(goal));
            }
        }catch(org.json.JSONException e){
            Log.e("Rica", "Bad JSON");
        }
        catch(Exception e){
            Log.e("Rica", e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    static Goal GoalFromJson(String json){
        Goal goal = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String title = jsonObject.has("Title")?jsonObject.getString("Title"):"";
            String subtitle = jsonObject.has("Subtitle")?jsonObject.getString("Subtitle"):"";
            String category = jsonObject.has("Category")?jsonObject.getString("Category"):"General";
            String goalType = jsonObject.has("Type")?jsonObject.getString("Type"):GoalTypes.SINGLE;
            boolean hasReminders = jsonObject.has("Reminders") && jsonObject.getString("Reminders").equals("T");
            String reminderType = jsonObject.has("ReminderType")?jsonObject.getString("ReminderType"):GoalReminderType.DAILY;
            String reminderData =  jsonObject.has("ReminderData")?jsonObject.getString("ReminderData"):"0";
            String timeData = jsonObject.has("TimeData")?jsonObject.getString("TimeData"):"";
            boolean complete = jsonObject.has("Complete") && jsonObject.getString("Complete").equals("T");

            goal = new Goal(title, subtitle);
            goal.category =     category!=null? category:"General";
            goal.goalType =     goalType!=null? goalType:GoalTypes.SINGLE;
            goal.hasReminders = hasReminders;
            goal.reminderType = reminderType!=null? reminderType:GoalReminderType.DAILY;
            goal.reminderData = reminderData!=null? reminderData:"0";
            goal.timeData =     timeData!=null? timeData:"";
            goal.complete =     complete;

            Log.e("Rica",goal.toString());
        }catch(org.json.JSONException e){
            Log.e("Rica", "Bad JSON");
        }
        catch(Exception e){
            Log.e("Rica", e.getMessage());
            e.printStackTrace();
        }
        return goal;
    }

    static int getNextGoalId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int id = sharedPreferences.getInt(context.getResources().getString(R.string.shared_goal_id),0);
        editor.putInt(context.getResources().getString(R.string.shared_goal_id),id+1);
        editor.apply();
        return id;
    }

    static void scheduleJob(){

    }
}
