package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 4/08/2015.
 */
public class SimpleIconButton extends ImageButton {

    //region inner field
    private Paint circlePaint;
    private float width;
    private float height;
    private float radius;
    //endregion

    //region constructor
    public SimpleIconButton(Context context) {
        super(context);
        init(null, 0);
    }

    public SimpleIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SimpleIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    //endregion

    //region setup
    private void init(AttributeSet attrs, int defStyleAttr) {
        getViewResources();
        if(attrs != null) {
            setAttributes(attrs, defStyleAttr);
        }else{
            setDefaultAttributes();
        }
    }
    //endregion

    //region helper
    private void getViewResources(){
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAlpha(10);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setAttributes(AttributeSet set, int defStyleAttr) {

        int[] attrs = new int[]{android.R.attr.adjustViewBounds, android.R.attr.scaleType, android.R.attr.background};
        TypedArray attributes = getContext().obtainStyledAttributes(set, attrs, defStyleAttr, 0);
        boolean adjustViewBounds = attributes.getBoolean(0, true);
        ScaleType scaleType = ScaleType.values()[attributes.getInt(1, ScaleType.CENTER_INSIDE.ordinal())];
        attributes.recycle();

        setAdjustViewBounds(adjustViewBounds);
        setScaleType(scaleType);
        setBackgroundResource(R.color.transparent);

        if (isInEditMode()) {
            setImageResource(R.drawable.ic_accept);
        }
    }

    private void setDefaultAttributes(){
        setAdjustViewBounds(true);
        setScaleType(ScaleType.CENTER_INSIDE);
        setBackgroundResource(R.color.transparent);
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
    }
    //endregion
}
