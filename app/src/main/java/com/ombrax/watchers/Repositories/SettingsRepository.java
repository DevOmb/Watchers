package com.ombrax.watchers.Repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.ombrax.watchers.Database.DatabaseKey;
import com.ombrax.watchers.Database.WatchersDatabase;
import com.ombrax.watchers.Utils.DatabaseUtils;

/**
 * Created by Ombrax on 31/08/2015.
 */
public class SettingsRepository {

    //region singleton
    private static SettingsRepository instance = new SettingsRepository();

    public static SettingsRepository getInstance() {
        return instance;
    }

    private SettingsRepository() {
        database = WatchersDatabase.getInstance();
    }
    //endregion

    //region inner field
    WatchersDatabase database;
    //endregion

    //region method
    public void update(String key, int value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseKey.SETTINGS_COLUMN_VALUE, value);
        database.getWritableDatabase().update(DatabaseKey.TABLE_SETTINGS, contentValues, DatabaseKey.SETTINGS_COLUMN_KEY + "= ?", DatabaseUtils.asArgs(key));
    }

    public int get(String key) {
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT " + DatabaseKey.SETTINGS_COLUMN_VALUE + " FROM " + DatabaseKey.TABLE_SETTINGS + " WHERE " + DatabaseKey.SETTINGS_COLUMN_KEY + " = ? ", DatabaseUtils.asArgs(key));
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(DatabaseKey.SETTINGS_COLUMN_VALUE));
    }
    //endregion
}
