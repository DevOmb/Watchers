package com.ombrax.watchers.Database;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class WatchersDatabase extends SQLiteAssetHelper {

    //region constant
    private static final String DATABASE_NAME = "watchers.db3";
    private static final int DATABASE_VERSION = 1;
    //endregion

    //region singleton
    private static WatchersDatabase database;

    private WatchersDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initialize(Context context) {
        database = new WatchersDatabase(context);
    }

    public static WatchersDatabase getInstance() {
        return database;
    }
    //endregion
}
