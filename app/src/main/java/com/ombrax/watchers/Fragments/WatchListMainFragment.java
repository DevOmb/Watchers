package com.ombrax.watchers.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;

/**
 * Created by Ombrax on 24/08/2015.
 */
public class WatchListMainFragment extends WatchListFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    //region create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        watchModels = WatchRepository.getInstance().getAll();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        mc.onMenuItemSelect(MenuItemType.HOME);
        toolbarManager.setExpandingTitle(getString(R.string.app_name));
    }
    //endregion

    //region interface implementation
    @Override
    public void onGlobalLayout() {
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            toolbarManager.setToolbarExpanded(true);
            System.out.println("Expanding");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }
    //endregion
}
