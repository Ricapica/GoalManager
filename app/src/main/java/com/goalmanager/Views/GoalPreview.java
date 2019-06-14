package com.goalmanager.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.goalmanager.R;

public class GoalPreview extends View {

    Paint paint;
    Context context;

    String goalTitle;
    String goalDescription;

    public GoalPreview(Context context) {
        super(context);
        paint = new Paint();
        this.context = context;
    }
    public GoalPreview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public GoalPreview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas){
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();


        //Background Color
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(context.getColor(R.color.button));

        canvas.drawRect(0,viewHeight/40f,viewWidth,(int)(viewHeight*.95),paint);
//        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);


        //Outline Color
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,(int)(viewHeight/40f),viewWidth,(int)(viewHeight*.95),paint);


        //Text color
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize((int)(viewHeight/3f));

        canvas.drawText((goalTitle.length()>22)?goalTitle.substring(0,22)+"…":goalTitle, (int)(viewWidth/25f), (int)(viewHeight/4*1.25), paint);

        paint.setTextSize((int)(viewHeight/4f));
        paint.setColor(context.getColor(R.color.DarkerGrey));
        canvas.drawText((goalDescription.length()>34)?goalDescription.substring(0,34)+"…":goalDescription, (int)(viewWidth/25f), (int)(viewHeight/4*1.25)*2, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension((int)(width*.8),height);
    }
}
