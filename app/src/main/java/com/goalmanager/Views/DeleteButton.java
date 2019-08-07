package com.goalmanager.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.goalmanager.R;

public class DeleteButton extends View {

    Paint paint;
    Context context;

    float widthRatio = .2f;
    float heightRatio = 1;

    public DeleteButton(Context context) {
        super(context);
    }
    public DeleteButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DeleteButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        this.context = context;
    }

    public void SetWidthHeightRatio(float widthRatio, float heightRatio){
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();


        //Background Color
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(context.getColor(R.color.delete_button));

        canvas.drawRect(0,viewHeight/40f,viewWidth,(int)(viewHeight*.95),paint);
//        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);


        //Outline Color
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,viewHeight/40f,viewWidth,(int)(viewHeight*.95),paint);


        //Text color
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize((int)(viewHeight/5f));

        canvas.drawText(context.getResources().getString(R.string.goal_view_delete), (int)(viewWidth/5f), (int)(viewHeight/4f*2), paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension((int)(width*widthRatio),(int)(height*heightRatio));
    }
}
