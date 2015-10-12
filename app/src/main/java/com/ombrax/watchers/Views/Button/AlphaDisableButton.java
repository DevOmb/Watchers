package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Ombrax on 10/10/2015.
 */
public class AlphaDisableButton extends ImageButton {

    public AlphaDisableButton(Context context) {
        super(context);
    }

    public AlphaDisableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaDisableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1f : 0.5f);
    }
}
