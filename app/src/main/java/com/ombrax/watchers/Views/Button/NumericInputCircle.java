package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;


/**
 * Created by Ombrax on 13/09/2015.
 */
public class NumericInputCircle extends EditText implements View.OnFocusChangeListener {

    //region variable
    private OnInputListener onInputListener;
    //endregion

    //region constructor
    public NumericInputCircle(Context context) {
        super(context);
        init();
    }

    public NumericInputCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumericInputCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //endregion

    //region setup
    private void init() {
        setOnFocusChangeListener(this);
        setSingleLine(true);
        setGravity(Gravity.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setBackgroundResource(R.drawable.round_input_background);
        setHint("0");
    }
    //endregion

    //region setter
    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    public void reset(){
        setSelected(false);
    }
    //endregion

    //region interface implementation
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String input = getText().toString();
            if (input != null && !input.isEmpty()) {
                int formatInput = Integer.parseInt(input);
                setSelected(formatInput == 0);
                if (onInputListener != null) {
                    if (formatInput != 0) {
                        onInputListener.onValidInput(formatInput);
                    } else {
                        onInputListener.onInvalidInput();
                    }
                }
            } else {
                setSelected(true);
                if (onInputListener != null) {
                    onInputListener.onInvalidInput();
                }
            }
        }
    }
    //endregion

    //region measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredDimension = Math.round(Math.min(getMeasuredWidth(), getMeasuredHeight()));
        setMeasuredDimension(measuredDimension, measuredDimension);
    }
    //endregion

    //region interface
    public interface OnInputListener {
        void onValidInput(int input);

        void onInvalidInput();
    }
    //endregion
}
