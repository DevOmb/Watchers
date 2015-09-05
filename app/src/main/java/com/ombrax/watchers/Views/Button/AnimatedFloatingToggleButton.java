package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.devomb.floatingtogglebutton.FloatingToggleButton;

/**
 * Created by Maikel on 19/06/2015.
 */
public class AnimatedFloatingToggleButton extends FloatingToggleButton implements FloatingToggleButton.OnToggleChangeListener {

    //region declaration

    //region constant
    private static final int ROTATE_DURATION = 300;
    private static final float ROTATE_DESTINATION = 135.0f;

    private static final int STROKE_WIDTH = 2;
    private static final float P = STROKE_WIDTH / 2;

    private static final float ratio1_4 = 0.25f;
    private static final float ratio3_4 = 0.75f;
    private static final float ratio5_12 = 0.4166666f;
    private static final float ratio7_12 = 0.5833333f;
    //endregion

    //region inner field
    private Paint borderPaint;

    private float width, height;
    private boolean allowInvalidate;
    private boolean initialState;

    private RotateAnimation startRotateAnimation;
    private RotateAnimation reverseRotateAnimation;

    private FloatingToggleButton.OnToggleChangeListener onToggleChangeListener;
    //endregion

    //endregion

    //region constructor
    public AnimatedFloatingToggleButton(Context context) {
        super(context);
        init();
    }

    public AnimatedFloatingToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedFloatingToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //endregion

    //region setup
    private void init() {
        initialState = true;
        borderPaint = newBorderPaint();
        startRotateAnimation = newRotateAnimation(true, ROTATE_DURATION);
        reverseRotateAnimation = newRotateAnimation(false, ROTATE_DURATION);
        super.setOnToggleChangeListener(this);
    }
    //endregion

    //region draw and measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO check draw on top + invalidate on resume?
        if (!initialState && allowInvalidate) {
            canvas.drawLine(width * ratio1_4 - P, height * ratio1_4, width * ratio5_12 - P, height * ratio1_4, borderPaint);
            canvas.drawLine(width * ratio7_12 + P, height * ratio1_4, width * ratio3_4 + P, height * ratio1_4, borderPaint);
            canvas.drawLine(width * ratio1_4, height * ratio1_4 - P, width * ratio1_4, height * ratio5_12 - P, borderPaint);
            canvas.drawLine(width * ratio3_4, height * ratio1_4 - P, width * ratio3_4, height * ratio5_12 - P, borderPaint);
            canvas.drawLine(width * ratio1_4, height * ratio7_12 + P, width * ratio1_4, height * ratio3_4 + P, borderPaint);
            canvas.drawLine(width * ratio3_4, height * ratio7_12 + P, width * ratio3_4, height * ratio3_4 + P, borderPaint);
            canvas.drawLine(width * ratio1_4 - P, height * ratio3_4, width * ratio5_12 - P, height * ratio3_4, borderPaint);
            canvas.drawLine(width * ratio7_12 + P, height * ratio3_4, width * ratio3_4 + P, height * ratio3_4, borderPaint);
            canvas.rotate(45.0f, width / 2, height / 2);
        }
        super.onDraw(canvas);
        /*
         ____________________
        |                    |
        |     _1_    _2_     |
        |   3|          |4   |
        |                    |
        |                    |
        |   5|___    ___|6   |
        |      7      8      |
        |____________________|

        */
    }
    //endregion

    //region helper
    private Paint newBorderPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    private RotateAnimation newRotateAnimation(final boolean fromInitialState, int duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromInitialState ? 0.0f : ROTATE_DESTINATION, fromInitialState ? ROTATE_DESTINATION : 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(getContext(), android.R.anim.accelerate_interpolator);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (fromInitialState) {
                    allowInvalidate = true;
                    postInvalidateDelayed(100);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return rotateAnimation;
    }
    //endregion

    //region override
    @Override
    public void setOnToggleChangeListener(OnToggleChangeListener onToggleChangeListener) {
        this.onToggleChangeListener = onToggleChangeListener;
    }

    @Override
    public void toggleAnimateVisibility() {
        allowInvalidate = !initialState;
        super.toggleAnimateVisibility();
    }

    @Override
    protected void onAnimateVisibilityChangeStarted(boolean goingOffScreen) {
        allowInvalidate = !goingOffScreen && !initialState;
        super.onAnimateVisibilityChangeStarted(goingOffScreen);
    }

    @Override
    protected void onAnimateVisibilityChangeEnded(boolean isOffScreen) {
        allowInvalidate = !isOffScreen && !initialState;
        super.onAnimateVisibilityChangeEnded(isOffScreen);
    }

    //endregion

    //region interface implementation
    @Override
    public void onToggleChange(boolean isEnabled) {
        initialState = !isEnabled;
        allowInvalidate = !isEnabled;
        if (isEnabled) {
            startAnimation(startRotateAnimation);
        } else {
            startAnimation(reverseRotateAnimation);
        }
        if (onToggleChangeListener != null) {
            onToggleChangeListener.onToggleChange(isEnabled);
        }
    }
    //endregion
}
