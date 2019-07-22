package com.goalmanager.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.goalmanager.R;

public class WeekDaysToggleButton extends View {

    Paint paint;
    Context context;

    public boolean state;
    public String day="U";          //U is for unassigned. It should not appear to the user.

    public WeekDaysToggleButton(Context context) {
        super(context);
        paint = new Paint();
        this.context = context;
        state = false;
    }

    public WeekDaysToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        this.context = context;
        state = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        //Background Color
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(context.getColor(R.color.white));

//        canvas.drawCircle(viewHeight/2f,viewHeight/2f,viewHeight*.5f,paint);


        //Outline Color
        if(state){
            paint.setColor(context.getColor(R.color.week_day_color));
        }else {
            paint.setColor(Color.BLACK);
        }
        paint.setStyle(Paint.Style.STROKE);

//        canvas.drawCircle(viewHeight/2f,viewHeight/2f,viewHeight*.5f,paint);

        //Text color
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize((int)(viewHeight/2f));

        canvas.drawText(day, (int)(viewWidth/4), (int)(viewHeight/1.5), paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension(width,height);
    }
}
