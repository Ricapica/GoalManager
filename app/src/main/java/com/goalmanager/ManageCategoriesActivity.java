package com.goalmanager;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.goalmanager.Views.CategoryView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ManageCategoriesActivity extends AppCompatActivity {

    ArrayList<String> categories;
    Context context;

    LinearLayout categoryListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        context = this;
        categoryListView = findViewById(R.id.categoryList);

        categories = LoadGoalCategories();

        ShowCategories();
    }

    public void ShowCategories(){
        categoryListView.removeAllViews();

        Button addCategoryButton = new Button(this);
        addCategoryButton.setText(getResources().getString(R.string.new_category_button));
        addCategoryButton.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addCategoryButton.setBackgroundColor(ContextCompat.getColor(this,R.color.add_category_button));
        createAddCategoryListeners(this, addCategoryButton);
        categoryListView.addView(addCategoryButton);

        Collections.sort(categories);
        //TODO- Deleting with prompt.
        for(String category:categories){
            if(category.equals("x") || category.equals("General")){
                continue;
            }
            Log.e("Building category view","progress");
            ViewGroup.LayoutParams layoutParams;
            layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = getApplicationContext().getResources().getDisplayMetrics().heightPixels/10;
            CategoryView categoryView = new CategoryView(context,null,category);
            categoryView.setLayoutParams(layoutParams);
            AddCategoryViewListeners(categoryView);
            categoryListView.addView(categoryView);
        }
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
                        //TODO- Error message if size to low.
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
    public void AddCategoryViewListeners(final CategoryView categoryView){
        categoryView.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.deletecategory_popup);

                final Button confirm_delete = dialog.findViewById(R.id.confirm_delete_button);
                confirm_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Rica"," Deleting Category");
                        categories.remove(categoryView.category);
                        SaveCategories(categories);
                        ShowCategories();
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
    public void addCategory(String categoryName){
        categories.add(categoryName);
        SaveCategories(categories);
        categoryListView.removeAllViews();
        ShowCategories();
    }
    private ArrayList<String> LoadGoalCategories(){
        ArrayList<String> categories = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        String allCategories = sharedPreferences.getString(context.getResources().getString(R.string.shared_categories),"");
        if(allCategories.length()>0) {
            String[] categoryArray = allCategories.split(";");
            categories.addAll(Arrays.asList(categoryArray));
        }
        else{
            categories.add("x");
            categories.add("General");
        }
        return categories;
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
}
