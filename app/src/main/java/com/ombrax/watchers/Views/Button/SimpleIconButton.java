package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 4/08/2015.
 */
public class SimpleIconButton extends ImageButton {

    //region inner field
    private int defaultPadding;
    //endregion

    //region constructor
    public SimpleIconButton(Context context) {
        super(context);
        init();
    }

    public SimpleIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //endregion

    //region setup
    private void init() {
        getDefaultResources();
        setDefaultAttributes();
    }
    //endregion

    //region helper
    private void getDefaultResources() {
        defaultPadding = getContext().getResources().getDimensionPixelSize(R.dimen.default_1x);
    }

    private void setDefaultAttributes() {
        setAdjustViewBounds(true);
        setScaleType(ScaleType.CENTER_INSIDE);
        setBackgroundResource(R.drawable.circle_background);
        setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
        if(isInEditMode()){
            setImageResource(R.drawable.ic_accept);
        }
    }
    //endregion
}
