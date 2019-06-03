package com.goalmanager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


public class GoalButton extends View {

    public Goal goal;

    private int circleCol, labelCol;
    private String circleText;
    private String buttonBodyText;
    private Paint circlePaint;
    private Context context;

    Button deleteButton;

    public GoalButton(Context context, AttributeSet attrs,Goal goal){
        super(context,attrs);
        this.context=context;
        this.goal = goal;
        circlePaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GoalButton,0 ,0);

        try{
            circleText = a.getString(R.styleable.GoalButton_circleLabel);
            circleCol = a.getInteger(R.styleable.GoalButton_circleColor,0);
            labelCol = a.getInteger(R.styleable.GoalButton_labelColor,0);
        }finally{
            a.recycle();
        }

        deleteButton = new Button(context);
    }

    public GoalButton(Context context, AttributeSet attrs, String s){
        super(context,attrs);
        this.context=context;


        circlePaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GoalButton,0 ,0);

        try{
            circleText = a.getString(R.styleable.GoalButton_circleLabel);
            circleCol = a.getInteger(R.styleable.GoalButton_circleColor,0);
            labelCol = a.getInteger(R.styleable.GoalButton_labelColor,0);
        }finally{
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas){
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();


        //Background Color
        circlePaint.setStyle(Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(context.getColor(R.color.button));

        canvas.drawRect(0,(int)(viewHeight/40),viewWidth,(int)(viewHeight*.95),circlePaint);
//        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);


        //Outline Color
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Style.STROKE);
        canvas.drawRect(0,(int)(viewHeight/40),viewWidth,(int)(viewHeight*.95),circlePaint);


        //Text color
        circlePaint.setStyle(Style.FILL_AND_STROKE);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setTextAlign(Paint.Align.LEFT);
        circlePaint.setTextSize((int)(viewHeight/3));

        canvas.drawText(goal.title, (int)(viewWidth/25), (int)(viewHeight/4*1.25), circlePaint);

        circlePaint.setTextSize((int)(viewHeight/4));
        canvas.drawText(goal.subtitle, (int)(viewWidth/25), (int)(viewHeight/4*1.25)*2, circlePaint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public int getCircleColor(){
        return circleCol;
    }

    public int getLabelColor(){
        return labelCol;
    }

    public String getLabelText(){
        return circleText;
    }
    public void setCircleColor(int newColor){
        //update the instance variable
        circleCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }
    public void setLabelColor(int newColor){
        //update the instance variable
        labelCol=newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }
    public void setLabelText(String newLabel){
        //update the instance variable
        circleText=newLabel;
        //redraw the view
        invalidate();
        requestLayout();
    }
    public void setBodyText(String newLabel){
        //update the instance variable
        buttonBodyText=newLabel;
        //redraw the view
        invalidate();
        requestLayout();
    }

}
