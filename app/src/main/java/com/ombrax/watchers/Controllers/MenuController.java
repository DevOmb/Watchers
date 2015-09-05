package com.ombrax.watchers.Controllers;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Interfaces.Handler.ISortMenuEnableHandler;
import com.ombrax.watchers.Interfaces.Observer.IOnSortMenuClosedListener;
import com.ombrax.watchers.Interfaces.Listener.IOnMenuItemClickListener;
import com.ombrax.watchers.Interfaces.Listener.IOnMenuItemSelectListener;
import com.ombrax.watchers.Interfaces.Listener.IOnSortOrderChangeListener;
import com.ombrax.watchers.Interfaces.Handler.IMenuCloseHandler;
import com.ombrax.watchers.Models.SortModel;

/**
 * Created by Ombrax on 6/08/2015.
 */
public class MenuController {

    //region inner field
    private boolean codeEnabled;
    //endregion

    //region singleton
    private static MenuController instance = new MenuController();

    public static MenuController getInstance() {
        return instance;
    }

    private MenuController() {
    }
    //endregion

    //region handler
    private IMenuCloseHandler menuCloseHandler;

    public void setMenuCloseHandler(IMenuCloseHandler menuCloseHandler) {
        this.menuCloseHandler = menuCloseHandler;
    }

    public void handleCloseMenu() {
        if (menuCloseHandler != null) {
            codeEnabled = true;
            menuCloseHandler.closeMenu();
        }
    }

    private ISortMenuEnableHandler sortMenuEnableHandler;

    public void setSortMenuEnableHandler(ISortMenuEnableHandler sortMenuEnableHandler) {
        this.sortMenuEnableHandler = sortMenuEnableHandler;
    }

    public void handleSortMenuEnable(boolean enable) {
        if (sortMenuEnableHandler != null) {
            sortMenuEnableHandler.handleSortMenuEnable(enable);
        }
    }
    //endregion

    //region listener
    //region main onClick
    private IOnMenuItemClickListener onMainMenuItemClickListener;

    public void setOnMainMenuItemClickListener(IOnMenuItemClickListener onMainMenuItemClickListener) {
        this.onMainMenuItemClickListener = onMainMenuItemClickListener;
    }

    public void onMainMenuItemClick(MenuItemType menuItemType) {
        if (menuItemType.childOf(MenuType.MAIN)) {
            if (onMainMenuItemClickListener != null) {
                onMainMenuItemClickListener.onMenuItemClick(menuItemType);
            }
        }
    }
    //endregion

    //region sort onChange
    private IOnSortOrderChangeListener onSortOrderChangeListener;

    public void setOnSortOrderChangeListener(IOnSortOrderChangeListener onSortOrderChangeListener) {
        this.onSortOrderChangeListener = onSortOrderChangeListener;
    }

    public void onSortOrderChange(SortModel sortModel) {
        if (onSortOrderChangeListener != null) {
            onSortOrderChangeListener.onSortOrderChange(sortModel);
        }
    }
    //endregion

    //region menu onSelect
    private IOnMenuItemSelectListener onSortMenuItemSelectListener;

    public void setOnSortMenuItemSelectListener(IOnMenuItemSelectListener onSortMenuItemSelectListener) {
        this.onSortMenuItemSelectListener = onSortMenuItemSelectListener;
    }

    private IOnMenuItemSelectListener onMainMenuItemSelectListener;

    public void setOnMainMenuItemSelectListener(IOnMenuItemSelectListener onMainMenuItemSelectListener) {
        this.onMainMenuItemSelectListener = onMainMenuItemSelectListener;
    }

    public void onMenuItemSelect(MenuItemType menuItemType) {
        if (menuItemType.childOf(MenuType.MAIN)) {
            if (onMainMenuItemSelectListener != null) {
                onMainMenuItemSelectListener.onMenuItemSelect(menuItemType);
            }
        } else if (menuItemType.childOf(MenuType.SORT)) {
            if (onSortMenuItemSelectListener != null) {
                onSortMenuItemSelectListener.onMenuItemSelect(menuItemType);
            }
        }
    }
    //endregion

    //region menu onSortClose
    private IOnSortMenuClosedListener onSortMenuClosedListener;

    public void setOnSortMenuClosedListener(IOnSortMenuClosedListener onSortMenuClosedListener) {
        this.onSortMenuClosedListener = onSortMenuClosedListener;
    }

    public void onSortMenuClosed() {
        if (onSortMenuClosedListener != null) {
            onSortMenuClosedListener.onSortMenuClosed(!codeEnabled);
        }
        codeEnabled = false;
    }
    //endregion

    //endregion
}
