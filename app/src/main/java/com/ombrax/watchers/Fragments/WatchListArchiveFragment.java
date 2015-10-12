package com.ombrax.watchers.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Repositories.WatchRepository;

/**
 * Created by Ombrax on 24/08/2015.
 */
public class WatchListArchiveFragment extends WatchListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        watchModels = WatchRepository.getInstance().getAll(WatchRepository.Mode.ARCHIVED);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(MenuItemType.ARCHIVE);
        disableToolbarScroll();
        toolbarManager.setExpandingTitleOnTransition("Archive", false);
    }

    private void disableToolbarScroll() {
        recyclerView.setNestedScrollingEnabled(false);
    }
}
