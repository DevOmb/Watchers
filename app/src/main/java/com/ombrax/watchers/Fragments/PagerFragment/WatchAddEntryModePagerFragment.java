package com.ombrax.watchers.Fragments.PagerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.devomb.cellgriddialog.CellGridDialog;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Views.Other.NumericInputField;

import java.util.Date;

/**
 * Created by Ombrax on 9/10/2015.
 */
public abstract class WatchAddEntryModePagerFragment extends Fragment implements CellGridDialog.OnAcceptListener {

    //region model
    protected WatchModel model;
    private Date lastViewed;
    //endregion

    //region protected view
    protected View view;
    protected TextView startAtLabel;
    protected TextView startAtErrorLabel;
    //endregion

    //region create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResourceId(), container, false);
        views(view);
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
    protected void setStartAtInputEnabled(boolean enabled) {
        if (enabled) {
            startAtLabel.setVisibility(View.VISIBLE);
            startAtErrorLabel.setVisibility(View.GONE);
        } else {
            startAtLabel.setVisibility(View.GONE);
            startAtErrorLabel.setVisibility(View.VISIBLE);
            startAtErrorLabel.setText(getStartAtErrorMessage());
        }
    }

    protected void updateStartAt() {
        startAtLabel.setText(model.format(WatchModel.Format.WATCH));
    }

    protected void registerKeyboardListener(NumericInputField inputField) {
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

    private void views(View view) {
        startAtLabel = (TextView) view.findViewById(R.id.fragment_add_start_at_label);
        startAtErrorLabel = (TextView) view.findViewById(R.id.fragment_add_start_at_error_label);
    }

    private void afterInit() {
        if (model != null) {
            updateStartAt();
        }
    }
    //endregion

    //region interface implementation
    @Override
    public void onAccept(int s, int e) {
        model.setCurrentSeason(s + 1);
        model.setCurrentEpisode(e + 1);
        updateStartAt();
    }
    //endregion
}
