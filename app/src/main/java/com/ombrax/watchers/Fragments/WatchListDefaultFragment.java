package com.ombrax.watchers.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;

/**
 * Created by Ombrax on 24/08/2015.
 */
public class WatchListDefaultFragment extends WatchListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setWatchModels(WatchRepository.getInstance().getAll());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarManager.setExpandingTitle(getString(R.string.app_name));
        mc.onMenuItemSelect(MenuItemType.HOME);
        expandToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
        collapseToolbar();
    }
}
