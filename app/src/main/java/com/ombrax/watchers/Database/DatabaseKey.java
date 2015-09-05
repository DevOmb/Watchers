package com.ombrax.watchers.Database;

public class DatabaseKey {

    //region table
    public static final String TABLE_WATCH = "tblwatch";
    public static final String TABLE_SETTINGS = "tblsettings";
    public static final String TABLE_SYSTEM = "tblsystem";
    //endregion

    //region column
    public static final String WATCH_COLUMN_ID =                    "id";
    public static final String WATCH_COLUMN_NAME =                  "name";
    public static final String WATCH_COLUMN_THUMBNAIL =             "thumbnail";
    public static final String WATCH_COLUMN_SEASON_COUNT =          "season_count";
    public static final String WATCH_COLUMN_SEASON_EPISODE_COUNT =  "season_episode_count";
    public static final String WATCH_COLUMN_SEASON_EPISODE_LIST =   "season_episode_list";
    public static final String WATCH_COLUMN_CURRENT_SEASON =        "current_season";
    public static final String WATCH_COLUMN_CURRENT_EPISODE =       "current_episode";
    public static final String WATCH_COLUMN_EPISODES_ONLY =         "episodes_only";
    public static final String WATCH_COLUMN_LAST_VIEWED =           "last_viewed";
    public static final String WATCH_COLUMN_ARCHIVED =              "archived";

    public static final String SETTINGS_COLUMN_KEY =                "key";
    public static final String SETTINGS_COLUMN_VALUE =              "value";

    public static final String SYSTEM_COLUMN_KEY =                  "key";
    public static final String SYSTEM_COLUMN_VALUE =                "value";
    //endregion

    //region settings
    public static final String SETTINGS_KEY_DISPLAY_PROGRESS =      "display_progress";
    public static final String SETTINGS_KEY_DISPLAY_BANNER =        "display_banner";
    public static final String SETTINGS_KEY_CONFIRM_COMPLETE =      "confirm_complete";
    public static final String SETTINGS_KEY_ACTION_COMPLETE =       "action_complete";
    public static final String SETTINGS_KEY_ACTION_SORT_CLOSE =     "action_sort_close";
    //endregion

    public static final String SYSTEM_KEY_SORT =                    "sort";

}
