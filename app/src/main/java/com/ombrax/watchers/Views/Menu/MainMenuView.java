package com.ombrax.watchers.Views.Menu;

import android.content.Context;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Interfaces.Command;

/**
 * Created by Ombrax on 7/08/2015.
 */
public class MainMenuView extends MenuView {

    //region constructor
    public MainMenuView(Context context) {
        super(context, MenuType.MAIN);
        init();
    }
    //endregion

    //region setup
    private void init() {
        mc.setOnMainMenuItemSelectListener(this);
    }
    //endregion

    //region method
    public void setMenuItemEnabled(final MenuItemType menuItemType, final boolean enabled) {
        waitForRecyclerViewContent(new Command() {
            @Override
            public void execute() {
                findViewByMenuItemType(menuItemType).setEnabled(enabled);
            }
        });
    }
    //endregion

    //region override
    @Override
    public void onMenuItemSelect(MenuItemType menuItemType) {
        switch (menuItemType) {
            case EXIT:
                return;
            case EDIT:
                setMenuItemEnabled(MenuItemType.EDIT, true);
        }
        if (selectedType == MenuItemType.EDIT && hasNewSelection(menuItemType)) {
            setMenuItemEnabled(MenuItemType.EDIT, false);
        }
        super.onMenuItemSelect(menuItemType);
    }
    //endregion
}
