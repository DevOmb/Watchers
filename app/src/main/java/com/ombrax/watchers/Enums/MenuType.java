package com.ombrax.watchers.Enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ombrax on 11/08/2015.
 */
public enum MenuType {
    //region enum
    MAIN,
    SORT;
    //endregion

    //region inner field
    private List<MenuItemType> children = new ArrayList<>();
    //endregion

    //region helper
    protected void addChild(MenuItemType child) {
        children.add(child);
    }
    //endregion

    //region method
    public List<MenuItemType> children() {
        return children;
    }
    //endregion
}
