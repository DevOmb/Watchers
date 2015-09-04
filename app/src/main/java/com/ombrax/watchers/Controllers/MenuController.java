package com.ombrax.watchers.Controllers;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Interfaces.Handler.ISecondaryMenuEnableHandler;
import com.ombrax.watchers.Interfaces.Observer.IOnUserSecondaryMenuCloseObserver;
import com.ombrax.watchers.Interfaces.Listener.IOnMenuItemClickListener;
import com.ombrax.watchers.Interfaces.Listener.IOnMenuItemSelectListener;
import com.ombrax.watchers.Interfaces.Listener.IOnSortMenuItemChangeListener;
import com.ombrax.watchers.Interfaces.Handler.IMenuCloseHandler;

import java.util.ArrayList;
import java.util.List;

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

    //region observer
    private List<IOnUserSecondaryMenuCloseObserver> onSecondaryMenuCloseObservers = new ArrayList<>();

    public void registerOnSecondaryMenuCloseObserver(IOnUserSecondaryMenuCloseObserver onSecondaryMenuCloseObserver){
        if(!onSecondaryMenuCloseObservers.contains(onSecondaryMenuCloseObserver)){
            onSecondaryMenuCloseObservers.add(onSecondaryMenuCloseObserver);
        }
    }

    public void unregisterOnSecondaryMenuCloseObserver(IOnUserSecondaryMenuCloseObserver onSecondaryMenuCloseObserver){
        onSecondaryMenuCloseObservers.remove(onSecondaryMenuCloseObserver);
    }

    public void notifyOnSecondaryMenuCloseObservers(){
        if(codeEnabled) {
            for (IOnUserSecondaryMenuCloseObserver observer : onSecondaryMenuCloseObservers) {
                observer.onUserSecondaryMenuClose();
            }
            codeEnabled = false;
        }
    }
    //endregion

    //region handler
    private IMenuCloseHandler menuCloseHandler;

    public void setMenuCloseHandler(IMenuCloseHandler menuCloseHandler) {
        this.menuCloseHandler = menuCloseHandler;
    }

    public void handleCloseMenu(){
        if(menuCloseHandler != null){
            codeEnabled = true;
            menuCloseHandler.closeMenu();
        }
    }

    private ISecondaryMenuEnableHandler secondaryMenuEnableHandler;

    public void setSecondaryMenuEnableHandler(ISecondaryMenuEnableHandler secondaryMenuEnableHandler) {
        this.secondaryMenuEnableHandler = secondaryMenuEnableHandler;
    }

    public void handleSecondaryMenuEnable(boolean enable){
        if(secondaryMenuEnableHandler != null){
            secondaryMenuEnableHandler.handleSecondaryMenuEnable(enable);
        }
    }
    //endregion

    //region listener
    //region main onClick
    private IOnMenuItemClickListener onMainMenuItemClickListener;

    public void setOnMainMenuItemClickListener(IOnMenuItemClickListener onMainMenuItemClickListener) {
        this.onMainMenuItemClickListener = onMainMenuItemClickListener;
    }

    public void onMainMenuItemClick(MenuItemType menuItemType){
        if(menuItemType.childOf(MenuType.MAIN)){
            if(onMainMenuItemClickListener != null){
                onMainMenuItemClickListener.onMenuItemClick(menuItemType);
            }
        }
    }
    //endregion

    //region sort onChange
    private IOnSortMenuItemChangeListener onSortMenuItemChangeListener;

    public void setOnSortMenuItemChangeListener(IOnSortMenuItemChangeListener onSortMenuItemChangeListener) {
        this.onSortMenuItemChangeListener = onSortMenuItemChangeListener;
    }

    public void onSortMenuItemChange(MenuItemType menuItemType, boolean isAscendingOrder) {
        if(menuItemType.childOf(MenuType.SORT)) {
            if (onSortMenuItemChangeListener != null) {
                onSortMenuItemChangeListener.onSortMenuItemChange(menuItemType, isAscendingOrder);
            }
        }
    }
    //endregion

    //region onSelect
    private IOnMenuItemSelectListener onSortMenuItemSelectListener;

    public void setOnSortMenuItemSelectListener(IOnMenuItemSelectListener onSortMenuItemSelectListener) {
        this.onSortMenuItemSelectListener = onSortMenuItemSelectListener;
    }

    private IOnMenuItemSelectListener onMainMenuItemSelectListener;

    public void setOnMainMenuItemSelectListener(IOnMenuItemSelectListener onMainMenuItemSelectListener) {
        this.onMainMenuItemSelectListener = onMainMenuItemSelectListener;
    }

    public void onMenuItemSelect(MenuItemType menuItemType){
        if(menuItemType.childOf(MenuType.MAIN)){
            if(onMainMenuItemSelectListener != null){
                onMainMenuItemSelectListener.onMenuItemSelect(menuItemType);
            }
        }else if(menuItemType.childOf(MenuType.SORT)) {
            if(onSortMenuItemSelectListener != null){
                onSortMenuItemSelectListener.onMenuItemSelect(menuItemType);
            }
        }
    }
    //endregion
    //endregion
}
