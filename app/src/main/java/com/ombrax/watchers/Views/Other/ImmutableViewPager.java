package com.ombrax.watchers.Views.Other;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ombrax on 10/10/2015.
 */
public class ImmutableViewPager extends ViewPager {

    public ImmutableViewPager(Context context) {
        super(context);
    }

    public ImmutableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View current = findViewWithTag("PF"+getCurrentItem());
        if (current != null) {
            current.measure(widthMeasureSpec, 0);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(current.getMeasuredHeight(), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
        requestLayout();
    }
}
