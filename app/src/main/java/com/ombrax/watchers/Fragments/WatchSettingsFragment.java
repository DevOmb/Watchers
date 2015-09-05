package com.ombrax.watchers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devomb.enumchoicedialog.EnumChoiceDialog;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Database.DatabaseKey;
import com.ombrax.watchers.Enums.ActionSetting;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Manager.SettingsManager;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.R;
import com.rey.material.widget.CheckBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchSettingsFragment extends Fragment {


    //region declaration
    //region inner field
    private MenuController mc;
    private ToolbarManager toolbarManager;
    private SettingsManager settingsManager;
    //endregion

    //region view
    private LinearLayout progressLayout;
    private LinearLayout bannerLayout;
    private LinearLayout confirmLayout;
    private LinearLayout completeLayout;
    private LinearLayout sortLayout;

    private CheckBox progressCheckbox;
    private CheckBox bannerCheckbox;
    private CheckBox confirmCompleteCheckbox;

    private TextView completeActionTextView;
    private TextView sortMenuActionTextView;
    //endregion
    //endregion

    //region create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mc = MenuController.getInstance();
        toolbarManager = ToolbarManager.getInstance();
        settingsManager = SettingsManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getViews(view);

        checkBoxSetup(progressLayout, progressCheckbox, DatabaseKey.SETTINGS_KEY_DISPLAY_PROGRESS);
        checkBoxSetup(bannerLayout, bannerCheckbox, DatabaseKey.SETTINGS_KEY_DISPLAY_BANNER);
        checkBoxSetup(confirmLayout, confirmCompleteCheckbox, DatabaseKey.SETTINGS_KEY_CONFIRM_COMPLETE);

        actionSetup(completeLayout, completeActionTextView, DatabaseKey.SETTINGS_KEY_ACTION_COMPLETE, ActionSetting.OnComplete.class, "Complete Action");
        actionSetup(sortLayout, sortMenuActionTextView, DatabaseKey.SETTINGS_KEY_ACTION_SORT_CLOSE, ActionSetting.OnSortMenuClose.class, "Sort Menu Close Action");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(MenuItemType.SETTINGS);
        mc.handleSortMenuEnable(false);
        toolbarManager.setExpandingTitle("Settings");
    }

    @Override
    public void onPause() {
        super.onPause();
        SettingsManager.getInstance().renew();
    }
    //endregion

    //region helper
    private void getViews(View view) {
        progressLayout = (LinearLayout) view.findViewById(R.id.fragment_settings_progress_layout);
        bannerLayout = (LinearLayout) view.findViewById(R.id.fragment_settings_banner_layout);
        confirmLayout = (LinearLayout) view.findViewById(R.id.fragment_settings_confirm_complete_layout);
        completeLayout = (LinearLayout) view.findViewById(R.id.fragment_settings_complete_action_layout);
        sortLayout = (LinearLayout) view.findViewById(R.id.fragment_settings_sort_action_layout);

        progressCheckbox = (CheckBox) view.findViewById(R.id.fragment_settings_progress_checkbox);
        bannerCheckbox = (CheckBox) view.findViewById(R.id.fragment_settings_banner_checkbox);
        confirmCompleteCheckbox = (CheckBox) view.findViewById(R.id.fragment_settings_confirm_complete_checkbox);

        completeActionTextView = (TextView) view.findViewById(R.id.fragment_settings_complete_action_label);
        sortMenuActionTextView = (TextView) view.findViewById(R.id.fragment_settings_sort_action_label);
    }

    private void checkBoxSetup(LinearLayout parentLayout, final CheckBox checkBox, final String databaseKey) {
        checkBox.setChecked(settingsManager.getBooleanSetting(databaseKey));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setBooleanSetting(databaseKey, isChecked);
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setCheckedImmediately(!checkBox.isChecked());
            }
        });
    }

    private <T extends Enum<T>> void actionSetup(LinearLayout parentLayout, final TextView actionLabel, final String databaseKey, final Class<T> enumClass, final String dialogTitle) {
        actionLabel.setText(enumClass.getEnumConstants()[settingsManager.getIntegerSetting(databaseKey)].name());
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndex = settingsManager.getIntegerSetting(databaseKey);
                new EnumChoiceDialog<T>(getContext())
                        .background(R.drawable.dark_dialog_background)
                        .title(dialogTitle, true)
                        .titleColor(R.color.holo_white)
                        .accentColor(R.color.accent)
                        .options(enumClass, selectedIndex)
                        .optionColor(R.color.holo_white)
                        .onAccept(new EnumChoiceDialog.OnAcceptListener() {
                            @Override
                            public void onAccept(Enum selectedEnum) {
                                actionLabel.setText(selectedEnum.name());
                                settingsManager.setIntegerSetting(databaseKey, selectedEnum.ordinal());
                            }
                        }).show();
            }
        });
    }
    //endregion
}
