package com.goalmanager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class Utils {
    static String SaveAsJSON(ArrayList<Goal> goals){
        String start="{\"Goals\":[";
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        for(Goal goal:goals){
            sb.append("{"); //Opening brace for a new object goal

            sb.append("\"Title\":\"").append(goal.title.replace("\"","'")).append("\",");
            sb.append("\"Subtitle\":\"").append(goal.subtitle.replace("\"","'")).append("\",");

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
            String title = jsonObject.getString("Title");
            String subtitle = jsonObject.getString("Subtitle");
            goal = new Goal(title, subtitle);

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
}
