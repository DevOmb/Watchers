package com.ombrax.watchers.Manager;

import com.ombrax.watchers.Database.DatabaseKey;
import com.ombrax.watchers.Enums.ActionSetting;
import com.ombrax.watchers.Repositories.SettingsRepository;
import com.ombrax.watchers.Utils.DatabaseUtils;

/**
 * Created by Ombrax on 31/08/2015.
 */
public class SettingsManager {

    //region inner field
    private SettingsRepository settingsRepository;

    private boolean displayProgressEnabled;
    private boolean displayBannerEnabled;
    private boolean confirmOnCompleteEnabled;
    private ActionSetting.OnSortMenuClose sortMenuCloseAction;
    private ActionSetting.OnComplete completeAction;
    //endregion

    //region singleton
    private static SettingsManager instance = new SettingsManager();

    public static SettingsManager getInstance() {
        return instance;
    }

    private SettingsManager() {
        settingsRepository = SettingsRepository.getInstance();
    }
    //endregion

    //region method
    public void renew() {
        displayProgressEnabled = DatabaseUtils.formatIntegerToBoolean(settingsRepository.get(DatabaseKey.SETTINGS_KEY_DISPLAY_PROGRESS));
        displayBannerEnabled = DatabaseUtils.formatIntegerToBoolean(settingsRepository.get(DatabaseKey.SETTINGS_KEY_DISPLAY_BANNER));
        confirmOnCompleteEnabled = DatabaseUtils.formatIntegerToBoolean(settingsRepository.get(DatabaseKey.SETTINGS_KEY_CONFIRM_COMPLETE));
        sortMenuCloseAction = ActionSetting.OnSortMenuClose.values()[settingsRepository.get(DatabaseKey.SETTINGS_KEY_ACTION_SORT_CLOSE)];
        completeAction = ActionSetting.OnComplete.values()[settingsRepository.get(DatabaseKey.SETTINGS_KEY_ACTION_COMPLETE)];
    }
    //endregion

    //region getter
    public boolean isDisplayProgressEnabled() {
        return displayProgressEnabled;
    }

    public boolean isDisplayBannerEnabled() {
        return displayBannerEnabled;
    }

    public boolean isConfirmOnCompleteEnabled() {
        return confirmOnCompleteEnabled;
    }

    public ActionSetting.OnSortMenuClose getOnSortMenuCloseAction() {
        return sortMenuCloseAction;
    }

    public ActionSetting.OnComplete getOnCompleteAction() {
        return completeAction;
    }
    //endregion

    //region delegate
    public int getIntegerSetting(String key) {
        return settingsRepository.get(key);
    }

    public boolean getBooleanSetting(String key) {
        return DatabaseUtils.formatIntegerToBoolean(getIntegerSetting(key));
    }

    public void setIntegerSetting(String key, Integer value) {
        settingsRepository.update(key, value);
    }

    public void setBooleanSetting(String key, boolean value) {
        setIntegerSetting(key, DatabaseUtils.formatBooleanToInteger(value));
    }
    //endregion
}
