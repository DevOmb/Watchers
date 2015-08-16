package com.ombrax.watchers.Manager;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Models.WatchModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ombrax on 11/08/2015.
 */
public class SortingManager {

    //region singleton
    private static SortingManager instance = new SortingManager();

    public static SortingManager getInstance() {
        return instance;
    }

    private SortingManager() {
        init();
    }
    //endregion

    //region inner field
    private Map<MenuItemType, Comparator<WatchModel>> sortingMap = new HashMap<>();
    //endregion

    //region setup
    private void init() {
        sortingMap.put(MenuItemType.ALPHABETICAL, new Comparator<WatchModel>() {
            @Override
            public int compare(WatchModel first, WatchModel second) {
                return first.getName().compareTo(second.getName());
            }
        });

        sortingMap.put(MenuItemType.DATE, new Comparator<WatchModel>() {
            @Override
            public int compare(WatchModel first, WatchModel second) {
                Date fDate = first.getLastViewed() == null ? new Date(0) : first.getLastViewed();
                Date sDate = second.getLastViewed() == null ? new Date(0) : second.getLastViewed();
                return fDate.compareTo(sDate);
            }
        });

        sortingMap.put(MenuItemType.PROGRESS, new Comparator<WatchModel>() {
            @Override
            public int compare(WatchModel first, WatchModel second) {
                return first.getProgress().compareTo(second.getProgress());
            }
        });

        sortingMap.put(MenuItemType.RUN_TIME, new Comparator<WatchModel>() {
            @Override
            public int compare(WatchModel first, WatchModel second) {
                return first.getTotalEpisodeCount().compareTo(second.getTotalEpisodeCount());
            }
        });
    }
    //endregion

    //region method
    public Comparator<WatchModel> get(MenuItemType menuItemType, boolean isAscendingOrder) {
        return isAscendingOrder ? sortingMap.get(menuItemType) : Collections.reverseOrder(sortingMap.get(menuItemType));
    }
    //endregion
}
