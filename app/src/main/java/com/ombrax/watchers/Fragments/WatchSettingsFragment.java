package com.ombrax.watchers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchSettingsFragment extends Fragment {

    private MenuController mc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mc = MenuController.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(MenuItemType.SETTINGS);
    }
}
