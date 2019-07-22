package com.goalmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.goalmanager.Views.GoalButton;
import com.goalmanager.Views.WeekDaysToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ArrayList<Goal> goals;              //This list will be filled with all the goals that should appear on the screen.
    ArrayList<String> goalCategories;   //This list will be filled with all the categories a goal can belong to.
    ArrayList<String> goalTypes;        //This list will be filled with all the types a goal can be.
    ArrayList<String> reminderTypes;    //This list will be filled with all the types a goal reminder can be.
    LinearLayout mainList;              //This is the main View where the goals will appear.
    Context context;                    //The context of this activity. Needed to pass to other classes.

    //TODO- Online Syncing support.
    //TODO- Manage categories activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mainList = findViewById(R.id.mainList);


        goals = new ArrayList<>();

        //Create buttons from the list of goals.

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        String goalString = sharedPreferences.getString("goals","");
        goals = Utils.LoadFromJSON(goalString);
        goalCategories = LoadGoalCategories();
        goalTypes = GoalTypes.GetGoalTypes();
        reminderTypes = LoadReminderTypes();
        //Prepare the layout params for the buttons.
        showGoalButtons(context);
    }

    public void createGoalListeners(final Context context, final GoalButton b) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Rica", "clicked "+ b.goal.title);
                //TODO take use to an activity corresponding to goal.
                //TODO Support for sub goals.

                final Dialog dialog = new Dialog(context);

                BuildGoalLayout(dialog, b.goal);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        showGoalButtons(context);
                    }
                });

                //Display the Dialog and force its size to be most of the screen.
                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(lp);
            }
        });

        //When the button is long clicked, it should prompt the user to change its title or subtitle.
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.modifygoal_popup);

                ((EditText)dialog.findViewById(R.id.goalTitle)).setText(b.goal.title);
                ((EditText)dialog.findViewById(R.id.goalBody)).setText(b.goal.subtitle);

                (dialog.findViewById(R.id.goalTitle)).requestFocus();
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                final Button add = dialog.findViewById(R.id.confirm_button);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String goalTitle=((EditText)dialog.findViewById(R.id.goalTitle)).getText().toString();
                        String goalBody=((EditText)dialog.findViewById(R.id.goalBody)).getText().toString();
                        if(!goalTitle.equals(""))
                        {
                            Log.e("Rica","Modifying it: ");
                            b.goal.title = goalTitle;
                            b.goal.subtitle = goalBody;
                            showGoalButtons(context);
                            dialog.dismiss();
                        }
                    }
                });

                final Button cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();


                return true;
            }
        });

        b.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.deletegoal_popup);

                final Button confirm_delete = dialog.findViewById(R.id.confirm_delete_button);

                confirm_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Rica"," Deleting");
                        goals.remove(b.goal);
                        showGoalButtons(context);
                        dialog.dismiss();
                    }
                });

                final Button cancel = dialog.findViewById(R.id.cancel_delete_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();



            }
        });
    }
    public void createAddGoalListeners(final Context context, final Button b) {

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Rica", "clicked "+ b.getText());

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.addbutton_popup);
                final Button add = dialog.findViewById(R.id.confirm_button);
                (dialog.findViewById(R.id.goalTitle)).requestFocus();
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String goalTitle=((EditText)dialog.findViewById(R.id.goalTitle)).getText().toString();
                        String goalBody=((EditText)dialog.findViewById(R.id.goalBody)).getText().toString();
                        if(!goalTitle.equals(""))
                        {
                            Log.e("Rica","Adding it: ");
                            addGoal(new Goal(goalTitle, goalBody));
                            showGoalButtons(context);
                            dialog.dismiss();
                        }
                    }
                });

                final Button cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();

            }
        });

    }
    public void createAddCategoryListeners(final Context context, final Button b) {

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Rica", "clicked "+ b.getText());

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_category_popup);
                final Button add = dialog.findViewById(R.id.confirm_button);
                (dialog.findViewById(R.id.categoryName)).requestFocus();
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categoryName=((EditText)dialog.findViewById(R.id.categoryName)).getText().toString();
                        if(categoryName.length()>1)
                        {
                            Log.e("Rica","Adding it: ");
                            addCategory(categoryName);
                            dialog.dismiss();
                        }
                    }
                });

                final Button cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();

            }
        });

    }

    public void addGoal(Goal g){
        g.category="General";           //Default Category.
        g.goalType=GoalTypes.SINGLE;    //Default Type.
        goals.add(g);
    }
    public void addCategory(String categoryName){
        goalCategories.add(categoryName);
        SaveCategories(goalCategories);
    }

    public void showGoalButtons(Context context){
        SaveGoals();
        mainList.removeAllViews();
        ViewGroup.LayoutParams layoutParams;
        layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = getApplicationContext().getResources().getDisplayMetrics().heightPixels/10;
        for(Goal g:goals){
            //Create the button.
            GoalButton goalButton = new GoalButton(this, null, g);

            //Give the button its layout params.
            goalButton.setLayoutParams(layoutParams);

            //Give the button what it should do when clicked.
            createGoalListeners(context, goalButton);

            //Add the button to the main View.
            mainList.addView(goalButton);
        }

        //Create the add goal button.
        Button addGoalButton = new Button(this);
        addGoalButton.setText(getResources().getString(R.string.new_goal_button));
        addGoalButton.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addGoalButton.setBackgroundColor(ContextCompat.getColor(this,R.color.add_goal_button));

        createAddGoalListeners(this, addGoalButton);

        Button addCategoryButton = new Button(this);
        addCategoryButton.setText(getResources().getString(R.string.new_category_button));
        addCategoryButton.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addCategoryButton.setBackgroundColor(ContextCompat.getColor(this,R.color.add_category_button));

        createAddCategoryListeners(this, addCategoryButton);

        String saved = Utils.SaveAsJSON(goals);
        Log.e("Saved: ",saved);
        Utils.LoadFromJSON(saved);
        mainList.addView(addGoalButton);
        mainList.addView(addCategoryButton);
    }

    private void SaveGoals(){
        String saveString = Utils.SaveAsJSON(goals);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.shared_goals), saveString);
        editor.apply();
    }

    private void SaveCategories(ArrayList<String> categories){
        StringBuilder allCategories = new StringBuilder();
        for(int i=0; i < categories.size();i++){
            allCategories.append(categories.get(i));
            if(i!=categories.size())
            {
                allCategories.append(";");
            }
        }
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.shared_categories), allCategories.toString());
        editor.apply();
    }

    private ArrayList<String> LoadGoalCategories(){
        ArrayList<String> categories = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        String allCategories = sharedPreferences.getString(context.getResources().getString(R.string.shared_categories),"");
        if(allCategories!=null && allCategories.length()>0) {
            String[] categoryArray = allCategories.split(";");
            categories.addAll(Arrays.asList(categoryArray));
        }
        else{
            categories.add("x");
            categories.add("General");
        }
        return categories;
    }
    private ArrayList<String> LoadReminderTypes(){
        ArrayList<String> reminderTypes = new ArrayList<>();
        reminderTypes.add("x");
        reminderTypes.addAll(GoalReminderType.GetReminderTypes());
        return reminderTypes;
    }


    private void BuildGoalLayout(final Dialog dialog, final Goal goal){
        dialog.setContentView(R.layout.goal_layout);

        //Initialize the Title and Description from the goal.
        TextView goalTitle = dialog.findViewById(R.id.goal_name);
        goalTitle.setText(goal.title);

        TextView goalDescription = dialog.findViewById(R.id.goal_description);
        goalDescription.setText(goal.subtitle);

        //Populate the category spinner.
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,goalCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner categorySpinner = dialog.findViewById(R.id.goal_category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String)categorySpinner.getItemAtPosition(position);
                Log.e("Spinner","New category selected: "+selected);
                if(selected.equals("x"))
                {
                    //Initialization
                    for(int i=0;i<categorySpinner.getAdapter().getCount();i++)
                    {
                        if(categorySpinner.getItemAtPosition(i).equals(goal.category)){
                            categorySpinner.setSelection(i);
                        }
                    }
                }else{
                    goal.category=selected;
                    SaveGoals();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Populate the type spinner.
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,goalTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner typeSpinner = dialog.findViewById(R.id.goal_type_spinner);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Spinner","New item selected"+typeSpinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Show Reminder Control if Valid.
        LinearLayout reminderView = dialog.findViewById(R.id.reminderViews);
        if(goal.SupportsReminders()) {
            final Switch reminder_switch = dialog.findViewById(R.id.toggleReminderSwitch);
            if(goal.hasReminders){
                reminder_switch.setChecked(true);

                reminderView.setVisibility(View.VISIBLE);
            }else{
                reminder_switch.setChecked(false);
                reminderView.setVisibility(View.GONE);
            }

            //When the switch is toggled.
            reminder_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reminder_switch.isChecked()) {
                        //ON
                        LinearLayout reminderView = dialog.findViewById(R.id.reminderViews);
                        reminderView.setVisibility(View.VISIBLE);
                        goal.hasReminders=true;
                        SaveGoals();
                    } else {
                        //OFF
                        LinearLayout reminderView = dialog.findViewById(R.id.reminderViews);
                        reminderView.setVisibility(View.GONE);
                        goal.hasReminders=false;
                        SaveGoals();
                    }
                }
            });

            //Populate the reminder type spinner.
            ArrayAdapter<String> reminderTypeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,reminderTypes);
            reminderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            final Spinner reminderSpinner = dialog.findViewById(R.id.reminder_type_spinner);
            reminderSpinner.setAdapter(reminderTypeAdapter);
            reminderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Spinner","New item selected"+reminderSpinner.getItemAtPosition(position));
                    String selected = (String)reminderSpinner.getItemAtPosition(position);
                    if(selected.equals("x"))
                    {
                        //Initialization
                        for(int i=0;i<reminderSpinner.getAdapter().getCount();i++)
                        {
                            if(reminderSpinner.getItemAtPosition(i).equals(goal.reminderType)){
                                reminderSpinner.setSelection(i);
                            }
                        }
                    }else{
                        goal.reminderType=selected;
                        SaveGoals();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //TODO-Make the 7 buttons a single viewGroup.
            final ArrayList<WeekDaysToggleButton> weekdayList = new ArrayList<>();
            //Adding weekday toggle buttons.
            ViewGroup.LayoutParams layoutParams;
            layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = getApplicationContext().getResources().getDisplayMetrics().heightPixels/20;
            layoutParams.width = (int)(getApplicationContext().getResources().getDisplayMetrics().widthPixels/7.5);

            WeekDaysToggleButton mondayToggle = new WeekDaysToggleButton(context);
            WeekDaysToggleButton tuesdayToggle = new WeekDaysToggleButton(context);
            WeekDaysToggleButton wednesdayToggle = new WeekDaysToggleButton(context);
            WeekDaysToggleButton thursdayToggle = new WeekDaysToggleButton(context);
            WeekDaysToggleButton fridayToggle = new WeekDaysToggleButton(context);
            WeekDaysToggleButton saturdayToggle = new WeekDaysToggleButton(context);
            WeekDaysToggleButton sundayToggle = new WeekDaysToggleButton(context);

            mondayToggle.setLayoutParams(layoutParams);
            tuesdayToggle.setLayoutParams(layoutParams);
            wednesdayToggle.setLayoutParams(layoutParams);
            thursdayToggle.setLayoutParams(layoutParams);
            fridayToggle.setLayoutParams(layoutParams);
            saturdayToggle.setLayoutParams(layoutParams);
            sundayToggle.setLayoutParams(layoutParams);


            mondayToggle.day = "M";
            tuesdayToggle.day = "T";
            wednesdayToggle.day = "W";
            thursdayToggle.day = "T";
            fridayToggle.day = "F";
            saturdayToggle.day = "S";
            sundayToggle.day = "S";
            if(goal.reminderData.charAt(0)=='0'){
                //No reminder options.
                Log.e("Rica","No reminder options for this goal.");
            } else if(goal.reminderData.charAt(0)=='1') {
                mondayToggle.state = goal.reminderData.charAt(1) == mondayToggle.day.charAt(0);
                tuesdayToggle.state = goal.reminderData.charAt(2) == tuesdayToggle.day.charAt(0);
                wednesdayToggle.state = goal.reminderData.charAt(3) == wednesdayToggle.day.charAt(0);
                thursdayToggle.state = goal.reminderData.charAt(4) == thursdayToggle.day.charAt(0);
                fridayToggle.state = goal.reminderData.charAt(5) == fridayToggle.day.charAt(0);
                saturdayToggle.state = goal.reminderData.charAt(6) == saturdayToggle.day.charAt(0);
                sundayToggle.state = goal.reminderData.charAt(7) == sundayToggle.day.charAt(0);
            }

            weekdayList.add(mondayToggle);
            weekdayList.add(tuesdayToggle);
            weekdayList.add(wednesdayToggle);
            weekdayList.add(thursdayToggle);
            weekdayList.add(fridayToggle);
            weekdayList.add(saturdayToggle);
            weekdayList.add(sundayToggle);

            for(final WeekDaysToggleButton day:weekdayList){
                day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        day.state=!day.state;
                        day.invalidate();
                        goal.reminderData=getReminderString(dialog,weekdayList);
                        SaveGoals();
                    }
                });
            }

            LinearLayout reminderDays = dialog.findViewById(R.id.reminderDays);
            reminderDays.addView(mondayToggle);
            reminderDays.addView(tuesdayToggle);
            reminderDays.addView(wednesdayToggle);
            reminderDays.addView(thursdayToggle);
            reminderDays.addView(fridayToggle);
            reminderDays.addView(saturdayToggle);
            reminderDays.addView(sundayToggle);

            //Get the time set
            final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
            if(goal.timeData.length()>0){
                timePicker.setHour(Integer.parseInt(goal.timeData.substring(0,2)));
                timePicker.setMinute(Integer.parseInt(goal.timeData.substring(2,4)));
            }else {
                goal.timeData = getTimeString(timePicker);
            }
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    Log.e("Called","Time picker");
                    goal.timeData = getTimeString(view);
                }
            });

            Button setReminder = dialog.findViewById(R.id.setReminder);
            setReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    jobScheduler.schedule(new JobInfo.Builder(0,
                            new ComponentName(context,JobManager.class))
                            .setMinimumLatency(5000)
                            .setTriggerContentMaxDelay(6000)
                            .setPersisted(true)
                            .build());
                    Log.e("Rica","clicked");
                }
            });

            final ToggleButton completeGoalButton = dialog.findViewById(R.id.complete_button);
            completeGoalButton.setChecked(goal.complete);

            completeGoalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goal.complete = completeGoalButton.isChecked();
                    SaveGoals();
                }
            });

        }
    }

    public String getReminderString(Dialog dialog, ArrayList<WeekDaysToggleButton> weekList){
        StringBuilder reply =new StringBuilder();
        Switch reminderSwitch = dialog.findViewById(R.id.toggleReminderSwitch);
        if(reminderSwitch.isChecked())
        {
            reply.append("1");
            for(WeekDaysToggleButton day:weekList){
                if(day.state){
                    reply.append(day.day.toUpperCase());
                }else{
                    reply.append(day.day.toLowerCase());
                }
            }
        }else{
            reply.append("0");
        }
        return reply.toString();
    }
    public String getTimeString(TimePicker timePicker){
        String hour = (""+timePicker.getHour()).length()==1?("0"+timePicker.getHour()):""+timePicker.getHour();
        String minute = (""+timePicker.getMinute()).length()==1?("0"+timePicker.getMinute()):""+timePicker.getMinute();
        Log.e("Rica","Time is: "+hour + minute);
        return hour + minute;
    }
}
