package com.ombrax.watchers.Repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.ombrax.watchers.Database.DatabaseKey;
import com.ombrax.watchers.Database.WatchersDatabase;
import com.ombrax.watchers.Utils.DatabaseUtils;

/**
 * Created by Ombrax on 31/08/2015.
 */
public class SystemRepository {

    //region singleton
    private static SystemRepository instance = new SystemRepository();

    public static SystemRepository getInstance() {
        return instance;
    }

    private SystemRepository() {
        database = WatchersDatabase.getInstance();
    }
    //endregion

    //region inner field
    WatchersDatabase database;
    //endregion

    //region method
    public void update(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseKey.SYSTEM_COLUMN_VALUE, value);
        database.getWritableDatabase().update(DatabaseKey.TABLE_SYSTEM, contentValues, DatabaseKey.SYSTEM_COLUMN_KEY + "= ?", DatabaseUtils.asArgs(key));
    }

    public String get(String key) {
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT " + DatabaseKey.SYSTEM_COLUMN_VALUE + " FROM " + DatabaseKey.TABLE_SYSTEM + " WHERE " + DatabaseKey.SYSTEM_COLUMN_KEY + " = ? ", DatabaseUtils.asArgs(key));
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(DatabaseKey.SYSTEM_COLUMN_VALUE));
    }
    //endregion
}
