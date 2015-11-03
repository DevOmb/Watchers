package com.ombrax.watchers.Enums;

import android.view.Menu;

/**
 * Created by Ombrax on 6/08/2015.
 */
public enum MenuItemType {

    //region enum
    HOME(MenuType.MAIN),
    ADD(MenuType.MAIN),
    EDIT(MenuType.MAIN),
    ARCHIVE(MenuType.MAIN),
    SETTINGS(MenuType.MAIN),
    EXIT(MenuType.MAIN),

    ALPHABETICAL(MenuType.SORT),
    RUN_TIME(MenuType.SORT),
    PROGRESS(MenuType.SORT),
    DATE(MenuType.SORT);
    //endregion

    //region inner field
    private MenuType mParent;
    //endregion

    //region constructor
    MenuItemType(MenuType parent) {
        mParent = parent;
        if (mParent != null) {
            mParent.addChild(this);
        }
    }
    //endregion

    //region method
    public boolean childOf(MenuType menuType){
        return mParent == menuType;
    }

    public MenuType parent() {
        return mParent;
    }
    //endregion

}
