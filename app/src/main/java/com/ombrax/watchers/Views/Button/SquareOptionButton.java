package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;

import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 4/08/2015.
 */
public class SquareOptionButton extends Button {

    //region inner field
    private Paint circlePaint;
    private float width;
    private float height;
    private float radius;
    //endregion

    //region constructor
    public SquareOptionButton(Context context) {
        super(context);
        init(null, 0);
    }

    public SquareOptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SquareOptionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    //endregion

    //region setup
    private void init(AttributeSet attrs, int defStyleAttr) {
        getViewResources();
    }
    //endregion

    //region helper
    private void getViewResources() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getResources().getColor(R.color.accent));
        circlePaint.setAlpha(221);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
    //endregion

    //region override
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isPressed()){
            canvas.drawCircle(width/2, height/2, radius, circlePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = Math.min(width, height)/2;
        int measuredDimension = Math.round(Math.min(width, height));
        setMeasuredDimension(measuredDimension, measuredDimension);
    }
    //endregion
}
