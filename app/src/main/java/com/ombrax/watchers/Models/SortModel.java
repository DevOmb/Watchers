package com.ombrax.watchers.Models;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;

/**
 * Created by Ombrax on 5/09/2015.
 */
public class SortModel {

    //region variable
    private MenuItemType sortType;
    private boolean isAscending;
    //endregion

    //region constructor
    public SortModel(MenuItemType sortType, boolean isAscending) {
        if (!sortType.childOf(MenuType.SORT)) {
            throw new IllegalArgumentException(MenuItemType.class.getSimpleName() + "." + sortType.name() + " is not part of the" + MenuType.class.getSimpleName() + "." + MenuType.SORT.name() + "collection");
        }
        this.sortType = sortType;
        this.isAscending = isAscending;
    }
    //endregion

    //region getter
    public MenuItemType getSortType() {
        return sortType;
    }

    public boolean isAscending() {
        return isAscending;
    }
    //endregion
}
