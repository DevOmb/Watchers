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
        setWatchModels(WatchRepository.getInstance().getAll(WatchRepository.Mode.ARCHIVED));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(MenuItemType.ARCHIVE);
        enableNestedScrolling(false);
        toolbarManager.setExpandingTitle("Archive");
    }

    @Override
    public void onSortMenuItemChange(MenuItemType menuItemType, boolean isAscendingOrder) {
        super.onSortMenuItemChange(menuItemType, isAscendingOrder);
        //TODO save to archive sort in db
    }
}
