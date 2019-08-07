package com.goalmanager.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryView extends ViewGroup {

    public String category;

    public TextView textView;
    public DeleteButton deleteButton;
    //Android Constructors.
    public CategoryView(Context context){
        super(context);
    }
    public CategoryView(Context context, AttributeSet attrs){
        super(context, attrs, 0);
    }
    public CategoryView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    //Custom Constructor.
    public CategoryView(Context context, AttributeSet attrs, String category){
        super(context,attrs,0);

        this.category = category;

        ViewGroup.LayoutParams layoutParams;
        layoutParams = new ViewGroup.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels/10;
        layoutParams.width = (int)(context.getResources().getDisplayMetrics().widthPixels*.2);
        textView = new TextView(context);
        textView.setText(category);

        layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels/10;
        layoutParams.width = (int)(context.getResources().getDisplayMetrics().widthPixels*.2);
        deleteButton = new DeleteButton(context, attrs);
        deleteButton.setLayoutParams(layoutParams);
        deleteButton.SetWidthHeightRatio(.15f,.75f);

        Log.e("Adding the views", " Right here right now");
        addView(textView);
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
