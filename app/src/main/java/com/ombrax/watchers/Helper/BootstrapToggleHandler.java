package com.ombrax.watchers.Helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ombrax.watchers.Models.WatchModel;

/**
 * Created by Ombrax on 17/09/2015.
 */
public class BootstrapToggleHandler<E extends Enum<E>> implements View.OnClickListener {

    //region inner field
    private E selectedEnum;
    private Button selectedToggle;
    //endregion

    //region variable
    private E[] enumConstants;
    private ViewGroup toggleContainer;
    private OnToggleSelectListener onToggleSelectListener;
    //endregion


    //region constructor
    public BootstrapToggleHandler(Class<E> enumClass, ViewGroup toggleContainer) {
        this.enumConstants = enumClass.getEnumConstants();
        this.toggleContainer = toggleContainer;
        init();
    }
    //endregion

    //region setup
    private void init() {
        for (int i = 0; i < toggleContainer.getChildCount(); i++) {
            if (i < enumConstants.length) {
                E enumC = enumConstants[i];
                Button toggle = (Button) toggleContainer.getChildAt(i);
                toggle.setText(enumC.toString());
                toggle.setTag(enumC);
                toggle.setOnClickListener(this);
            }
        }
        toggleContainer.getChildAt(0).performClick();
    }
    //endregion

    //region method
    public void selectAndDisable(int toggle){
        if(toggle >= toggleContainer.getChildCount()){
            return;
        }
        //Disable all
        for(int i = 0; i < toggleContainer.getChildCount(); i++){
            Button disableButton = (Button) toggleContainer.getChildAt(i);
            disableButton.setEnabled(false);
            disableButton.setAlpha(0.5f);
        }

        Button selectButton = (Button) toggleContainer.getChildAt(toggle);
        selectButton.setEnabled(true);
        selectButton.setAlpha(1f);
        selectButton.performClick();
    }
    //endregion

    //region getter setter
    public E getSelectedEnum(){
        return selectedEnum;
    }

    public void setOnToggleSelectListener(OnToggleSelectListener onToggleSelectListener) {
        this.onToggleSelectListener = onToggleSelectListener;
    }
    //endregion

    //region click listener
    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button toggle = (Button) v;
            if (toggle != selectedToggle) {
                if (selectedToggle != null) {
                    selectedToggle.setSelected(false);
                }
                selectedToggle = toggle;
                selectedToggle.setSelected(true);
                selectedEnum = (E) toggle.getTag();
                if(onToggleSelectListener != null){
                    onToggleSelectListener.onToggleSelect(selectedToggle, selectedEnum);
                }
            }
        }
    }
    //endregion

    //region interface
    public interface OnToggleSelectListener<E extends Enum<E>> {
        void onToggleSelect(Button selectedToggle, E selectedEnum);
    }
    //endregion
}
