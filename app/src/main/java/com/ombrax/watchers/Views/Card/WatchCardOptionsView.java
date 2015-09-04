package com.ombrax.watchers.Views.Card;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Views.Button.SimpleIconButton;

/**
 * Created by Ombrax on 22/08/2015.
 */
public class WatchCardOptionsView extends LinearLayout {

    //region variable
    private WatchModel watchModel;
    private Dialog parentDialog;
    //endregion

    //region inner field
    private DomainController dc;
    private WatchRepository repo;

    private int singlePadding;
    //endregion

    //region constructor
    public WatchCardOptionsView(Context context, WatchModel watchModel, Dialog parentDialog) {
        super(context);
        this.watchModel = watchModel;
        this.parentDialog = parentDialog;
        init();
    }
    //endregion

    //region setup
    private void init() {
        getViewResources();
        setAttributes();
        populate();
        dc = DomainController.getInstance();
        repo = WatchRepository.getInstance();
    }

    private void populate() {
        addOptionButton(R.drawable.ic_undo, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchModel.decrement()) {
                    repo.update(watchModel, true);
                    dc.handleWatchCardUpdate();
                }
            }
        });
        addOptionButton(R.drawable.ic_edit, new OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.notifyListItemObservers(watchModel);
                parentDialog.dismiss();
            }
        });
        addOptionButton(R.drawable.ic_archive, new OnClickListener() {
            @Override
            public void onClick(View v) {
                watchModel.setArchived(true);
                repo.update(watchModel, false);
                dc.handleWatchCardRemove(watchModel);
                parentDialog.dismiss();
            }
        });
        addOptionButton(R.drawable.ic_delete, new OnClickListener() {
            @Override
            public void onClick(View v) {
                repo.delete(watchModel.getId());
                dc.handleWatchCardRemove(watchModel);
                parentDialog.dismiss();
            }
        });
        addOptionButton(R.drawable.ic_cancel, new OnClickListener() {
            @Override
            public void onClick(View v) {
                parentDialog.dismiss();
            }
        });
    }
    //endregion

    //region helper
    private void getViewResources() {
        singlePadding = getResources().getDimensionPixelSize(R.dimen.default_1x);
    }

    private void setAttributes() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.color.dark_grey);
        setPadding(singlePadding, singlePadding, singlePadding, singlePadding);
    }

    private void addOptionButton(int imageResource, OnClickListener clickListener) {
        SimpleIconButton button = new SimpleIconButton(getContext());
        button.setLayoutParams(LayoutUtils.newLinearLayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        button.setImageResource(imageResource);
        button.setOnClickListener(clickListener);
        addView(button);
    }
    //endregion
}
