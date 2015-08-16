package com.ombrax.watchers.Models;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Manager.MenuManager;
import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 25/07/2015.
 */
public class MenuItem {

    //region enum-like values
    public static MenuItem Home = new MenuItem(MenuItemType.HOME, R.drawable.ic_home);
    public static MenuItem Add = new MenuItem(MenuItemType.ADD, R.drawable.ic_add);
    public static MenuItem Edit = new MenuItem(MenuItemType.EDIT, R.drawable.ic_edit);
    public static MenuItem Archive = new MenuItem(MenuItemType.ARCHIVE, R.drawable.ic_archive);
    public static MenuItem Settings = new MenuItem(MenuItemType.SETTINGS, R.drawable.ic_settings);
    public static MenuItem Exit = new MenuItem(MenuItemType.EXIT, R.drawable.ic_exit);

    public static MenuItem Alphabetical = new MenuItem(MenuItemType.ALPHABETICAL, "Name", R.drawable.ic_sort_alphabet);
    public static MenuItem Date = new MenuItem(MenuItemType.DATE, R.drawable.ic_sort_date);
    public static MenuItem Progress = new MenuItem(MenuItemType.PROGRESS, R.drawable.ic_sort_progress);
    public static MenuItem RunTime = new MenuItem(MenuItemType.RUN_TIME, "Run Time", R.drawable.ic_sort_time);
    //endregion

    //region variable
    private MenuItemType type;
    private String name;
    private int icon;
    //endregion

    //region constructor
    private MenuItem(MenuItemType type, String name, int icon) {
        this.type = type;
        this.name = name;
        this.icon = icon;
    }

    private MenuItem(MenuItemType type, int icon) {
        this(type, null, icon);
    }
    //endregion

    //region method
    public MenuItemType type() {
        return type;
    }

    public String name() {
        return name == null ? type.toString() : name;
    }

    public int icon() {
        return icon;
    }
    //endregion
}


