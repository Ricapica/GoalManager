package com.goalmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

import com.goalmanager.Views.GoalButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ArrayList<Goal> goals;              //This list will be filled with all the goals that should appear on the screen.
    ArrayList<String> goalCategories;   //This list will be filled with all the categories a goal can belong to.
    ArrayList<String> goalTypes;        //This list will be filled with all the types a goal can be.
    LinearLayout mainList;              //This is the main View where the goals will appear.
    Context context;                    //The context of this activity. Needed to pass to other classes.

    //TODO-Online Syncing support.

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

    public void addGoal(Goal g){
        goals.add(g);
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
        addGoalButton.setBackgroundColor(ContextCompat.getColor(this,R.color.add_button));

        createAddGoalListeners(this, addGoalButton);
        String saved = Utils.SaveAsJSON(goals);
        Log.e("Saved: ",saved);
        Utils.LoadFromJSON(saved);
        mainList.addView(addGoalButton);

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
            categories.add("General");
            SaveCategories(categories);
        }
        return categories;
    }



    private void BuildGoalLayout(Dialog dialog, Goal goal){
        dialog.setContentView(R.layout.goal_layout);

        //Initialize the Title and Description from the goal.
        TextView goalTitle = dialog.findViewById(R.id.goal_name);
        goalTitle.setText(goal.title);

        TextView goalDescription = dialog.findViewById(R.id.goal_description);
        goalDescription.setText(goal.subtitle);

        //Populate the category spinner.
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,goalCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner categorySpinner = dialog.findViewById(R.id.goal_category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Spinner","New item selected"+categorySpinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Populate the type spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,goalTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner typeSpinner = dialog.findViewById(R.id.goal_type_spinner);
        typeSpinner.setAdapter(adapter);
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

    }
}
