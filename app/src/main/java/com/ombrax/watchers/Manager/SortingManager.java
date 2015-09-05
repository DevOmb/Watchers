package com.ombrax.watchers.Manager;

import com.ombrax.watchers.Database.DatabaseKey;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Models.SortModel;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.Repositories.SystemRepository;
import com.ombrax.watchers.Utils.DatabaseUtils;

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
        systemRepository = SystemRepository.getInstance();
        comparatorSetup();
    }
    //endregion

    //region inner field
    private SystemRepository systemRepository;
    private Map<MenuItemType, Comparator<WatchModel>> sortingMap = new HashMap<>();
    //endregion

    //region helper
    private void comparatorSetup() {
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
    public Comparator<WatchModel> getComparator(SortModel sortModel) {
        return sortModel.isAscending() ? sortingMap.get(sortModel.getSortType()) : Collections.reverseOrder(sortingMap.get(sortModel.getSortType()));
    }

    public Comparator<WatchModel> getCurrentComparator() {
        SortModel sortModel = getSystemSort();
        return sortModel.isAscending() ? sortingMap.get(sortModel.getSortType()) : Collections.reverseOrder(sortingMap.get(sortModel.getSortType()));
    }

    public SortModel getSystemSort(){
        String model = systemRepository.get(DatabaseKey.SYSTEM_KEY_SORT);
        return DatabaseUtils.formatStringToSortModel(model);
    }

    public void updateSystemSort(SortModel sortModel){
        systemRepository.update(DatabaseKey.SYSTEM_KEY_SORT, DatabaseUtils.formatSortModelToString(sortModel));
    }
    //endregion
}
