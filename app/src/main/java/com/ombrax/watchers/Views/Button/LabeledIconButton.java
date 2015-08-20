package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 7/08/2015.
 */
public class LabeledIconButton extends LinearLayout {

    //region declaration
    //region variable
    private int iconResource;
    private String text;
    //endregion

    //region inner field
    private int defaultPadding;
    private ImageView imageView;
    private TextView textView;
    //endregion
    //endregion

    //region constructor
    public LabeledIconButton(Context context) {
        super(context);
        init();
    }

    public LabeledIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabeledIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //endregion

    //region setup
    private void init() {
        getDefaultResources();
        setDefaultAttributes();
        addIcon();
        addLabel();
    }
    //endregion


    //region getter setter
    //region getter
    public int getIconResource() {
        return iconResource;
    }

    public String getText() {
        return text;
    }
    //endregion

    //region setter
    public void setIconResource(int iconResource){
        this.iconResource = iconResource;
        imageView.setImageResource(iconResource);
    }

    public void setLabel(String text){
        this.text = text;
        textView.setText(text);
    }
    //endregion
    //endregion

    //region helper
    private void getDefaultResources() {
        defaultPadding = getResources().getDimensionPixelSize(R.dimen.default_1x);
    }

    private void setDefaultAttributes() {
        setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
        setBackgroundResource(R.drawable.default_menu_selector);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
    }

    private void addIcon() {
        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(0, 0, defaultPadding, 0);
        imageView.setClickable(false);
        if (isInEditMode()) {
            imageView.setImageResource(R.drawable.ic_accept);
        }
        addView(imageView);
    }

    private void addLabel() {
        textView = new TextView(getContext());
        textView.setAllCaps(true);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setClickable(false);
        if (isInEditMode()) {
            textView.setText("Accept");
        }
        addView(textView);
    }
    //endregion

    //region override
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1.0f : 0.5f);
    }
    //endregion
}
