package com.ombrax.watchers.Views.Button;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ombrax.watchers.R;


/**
 * Created by Ombrax on 13/09/2015.
 */
public class NumericInputField extends EditText implements View.OnFocusChangeListener {

    //region variable
    private OnFocusChangeListener onFocusChangeListener;
    //endregion

    //region constructor
    public NumericInputField(Context context) {
        super(context);
        init();
    }

    public NumericInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumericInputField(Context context, AttributeSet attrs, int defStyleAttr) {
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

    //region getter
    public boolean isEmpty() {
        return getText().toString().isEmpty() || Integer.parseInt(getText().toString()) == 0;
    }
    //endregion

    //region setter
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }
    //endregion

    //region method
    public void clean() {
        setSelected(false);
    }
    //endregion


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            dispatchKeyEvent(event);
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    //region interface implementation
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String input = getText().toString();
            if (input != null && !input.isEmpty()) {
                int formatInput = Integer.parseInt(input);
                setSelected(formatInput == 0);
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusLost(this, formatInput != 0, formatInput);
                }
            } else {
                setSelected(true);
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusLost(this, false, 0);
                }
            }
        }
    }
    //endregion

    //region interface
    public interface OnFocusChangeListener {
        void onFocusLost(EditText source, boolean hasValidInput, int value);
    }
    //endregion
}
