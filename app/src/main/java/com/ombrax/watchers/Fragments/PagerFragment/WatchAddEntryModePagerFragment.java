package com.ombrax.watchers.Fragments.PagerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ombrax.watchers.Enums.WatchState;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Views.Button.AlphaDisableButton;
import com.ombrax.watchers.Views.Button.NumericInputField;

import java.util.Date;

/**
 * Created by Ombrax on 9/10/2015.
 */
public abstract class WatchAddEntryModePagerFragment extends Fragment{

    //region model
    protected WatchModel model;
    private Date lastViewed;
    //endregion

    //region protected view
    protected View view;
    protected LinearLayout startAtContainer;
    protected TextView startAtErrorLabel;
    protected AlphaDisableButton startAtDecrement;
    protected TextView startAtLabel;
    protected AlphaDisableButton startAtIncrement;
    //endregion

    //region create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResourceId(), container, false);
        views(view);
        rootInit();
        init(view);//subclass init
        afterInit();
        return view;
    }
    //endregion

    //region abstract method
    protected abstract int getLayoutResourceId();

    protected abstract void init(View view);

    public abstract String getStartAtErrorMessage();
    //endregion

    //region getter setter
    public WatchModel getModel() {
        model.setLastViewed(lastViewed);
        return model;
    }

    public void setModel(WatchModel model) {
        this.model = model;
        lastViewed = (model == null ? null : model.getLastViewed());
    }
    //endregion

    //region helper
    //region subclass
    protected void setStartAtInputEnabled(boolean enabled) {
        if (enabled) {
            startAtContainer.setVisibility(View.VISIBLE);
            startAtErrorLabel.setVisibility(View.GONE);
        } else {
            startAtContainer.setVisibility(View.GONE);
            startAtErrorLabel.setVisibility(View.VISIBLE);
            startAtErrorLabel.setText(getStartAtErrorMessage());
        }
    }

    protected void updateStartAt(){
        startAtLabel.setText(model.format(WatchModel.Format.WATCH));
    }

    protected void registerKeyboardListener(NumericInputField inputField){
        inputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LayoutUtils.clearFocusOnKeyboardHide(view);
                }
                return false;
            }
        });
        inputField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    LayoutUtils.clearFocusOnKeyboardHide(view);
                }
                return false;
            }
        });
    }
    //endregion

    //region root
    private void views(View view) {
        startAtContainer = (LinearLayout) view.findViewById(R.id.fragment_add_start_at_container);
        startAtErrorLabel = (TextView) view.findViewById(R.id.fragment_add_start_at_error_label);
        startAtDecrement = (AlphaDisableButton) view.findViewById(R.id.fragment_add_start_at_decrement);
        startAtLabel = (TextView) view.findViewById(R.id.fragment_add_start_at_label);
        startAtIncrement = (AlphaDisableButton) view.findViewById(R.id.fragment_add_start_at_increment);
    }

    private void rootInit() {
        //Start At
        startAtDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.decrement()) {
                    updateStartAt();
                }
                setStartAtControlsEnabled();
            }
        });
        startAtIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.increment()) {
                    updateStartAt();
                }
                setStartAtControlsEnabled();
            }
        });
    }

    private void afterInit(){
        if(model != null){
            startAtLabel.setText(model.format(WatchModel.Format.WATCH));
            setStartAtControlsEnabled();
        }
    }

    private void setStartAtControlsEnabled() {
        startAtDecrement.setEnabled(model.getWatchState() != WatchState.FIRST_EPISODE);
        startAtIncrement.setEnabled(model.getWatchState() != WatchState.LAST_EPISODE);
    }
    //endregion
    //endregion
}
