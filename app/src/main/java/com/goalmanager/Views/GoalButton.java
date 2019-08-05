package com.goalmanager.Views;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.goalmanager.Goal;


public class GoalButton extends ViewGroup {

    public Goal goal;


    public GoalPreview goalPreview;
    public DeleteButton deleteButton;
    //Android Constructors.
    public GoalButton(Context context){
        super(context);
    }
    public GoalButton(Context context, AttributeSet attrs){
        super(context, attrs, 0);
    }
    public GoalButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Custom Constructor.
    public GoalButton(Context context, AttributeSet attrs,Goal goal){
        super(context,attrs, 0);

        this.goal = goal;

        ViewGroup.LayoutParams layoutParams;
        layoutParams = new ViewGroup.LayoutParams( LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels/10;
        layoutParams.width = (int)(context.getResources().getDisplayMetrics().widthPixels*.2);
        goalPreview = new GoalPreview(context, attrs, goal);
        goalPreview.goalTitle = goal.title;
        goalPreview.goalDescription = goal.subtitle;
        goalPreview.setLayoutParams(layoutParams);


        layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels/10;
        layoutParams.width = (int)(context.getResources().getDisplayMetrics().widthPixels*.2);
        deleteButton = new DeleteButton(context, attrs);
        deleteButton.setLayoutParams(layoutParams);


        addView(goalPreview);
        addView(deleteButton);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    //Prevent Scrolling.
    @Override
    public boolean shouldDelayChildPressedState(){
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;
        //get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;
        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;
        for(int i = 0; i<count;i++){

            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                return;

            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            if (curLeft + curWidth >= childRight) {
//                curLeft = childLeft;
//                curTop += maxHeight;
                maxHeight = 0;
            }
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            //store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }
    }

}
