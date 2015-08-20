package com.ombrax.watchers.Interfaces.Listener;

import com.ombrax.watchers.Enums.MenuItemType;

/**
 * Created by Ombrax on 10/08/2015.
 */
public interface IOnSortMenuItemChangeListener {
    void onSortMenuItemChange(MenuItemType menuItemType, boolean isAscendingOrder);
}
