package com.ombrax.watchers.Animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Ombrax on 12/07/2015.
 */
public class HeightAnimation extends Animation {

    //region declaration
    //region constant
    private final int DEFAULT_DURATION = 1000;
    //endregion

    //region inner field
    private Transition transition;
    private View view;
    private int height;
    private ViewGroup.LayoutParams layoutParams;
    //endregion
    //endregion

    //region constructor
    public HeightAnimation(View view, int height) {
        this.view = view;
        this.height = height;
        setDuration(DEFAULT_DURATION);
        transition = (view.getVisibility() == View.VISIBLE ? Transition.COLLAPSE : Transition.EXPAND);
        layoutParams = view.getLayoutParams();
        layoutParams.height = transition == Transition.EXPAND ? 0 : height;
    }

    public HeightAnimation(View view, int height, int duration) {
        this(view, height);
        setDuration(duration);
    }
    //endregion

    //region override
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (!hasStarted()) {
            view.setVisibility(View.VISIBLE);
        }
        if (interpolatedTime < 1.0f) {
            //Animating ...
            float weight = (transition == Transition.EXPAND ? interpolatedTime : (1 - interpolatedTime));
            layoutParams.height = (int) (height * weight);
            view.requestLayout();
        } else if (hasEnded()) {
            //Done animating
            if (transition == Transition.EXPAND) {
                layoutParams.height = height;
                view.requestLayout();
                transition = Transition.COLLAPSE;
            } else {
                view.setVisibility(View.GONE);
                transition = Transition.EXPAND;
            }
        }
    }
    //endregion

    //region enum
    private enum Transition {
        COLLAPSE,
        EXPAND
    }
    //endregion
}
