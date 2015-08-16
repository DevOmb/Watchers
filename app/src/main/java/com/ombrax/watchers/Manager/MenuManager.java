package com.ombrax.watchers.Manager;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Models.MenuItem;
import com.ombrax.watchers.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ombrax on 11/08/2015.
 */
public class MenuManager {

    //region singleton
    private static MenuManager instance = new MenuManager();

    public static MenuManager getInstance() {
        return instance;
    }

    private MenuManager() {
        init();
    }
    //endregion

    //region inner field
    private Map<MenuType, List<MenuItem>> menuItems = new HashMap<>();
    //endregion

    //region setup
    private void init() {
        for (MenuType menuType : MenuType.values()) {
            menuItems.put(menuType, new ArrayList<MenuItem>());
        }
        add(MenuItem.Home);
        add(MenuItem.Add);
        add(MenuItem.Edit);
        add(MenuItem.Archive);
        add(MenuItem.Settings);
        add(MenuItem.Exit);
        add(MenuItem.Alphabetical);
        add(MenuItem.Date);
        add(MenuItem.Progress);
        add(MenuItem.RunTime);
    }
    //endregion

    //region helper
    private void add(MenuItem menuItem) {
        menuItems.get(menuItem.type().parent()).add(menuItem);
    }
    //endregion

    //region method
    public List<MenuItem> getAll(MenuType menuType) {
        return menuItems.get(menuType);
    }
    //endregion

}
