package com.ombrax.watchers.Repositories;

import android.database.Cursor;

import com.ombrax.watchers.Database.DatabaseKey;
import com.ombrax.watchers.Database.WatchersDatabase;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.Database.WatchMapper;
import com.ombrax.watchers.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatchRepository{

    //region singleton
    private static final WatchRepository instance = new WatchRepository();

    private WatchRepository() {
        database = WatchersDatabase.getInstance();
    }

    public static WatchRepository getInstance(){
        return instance;
    }
    //endregion

    //region inner field
    WatchersDatabase database;
    //endregion

    //region method
    public void insert(WatchModel watchModel) {
        database.getWritableDatabase().insert(DatabaseKey.TABLE_WATCH, null, WatchMapper.contentValuesForWatch(watchModel));
    }

    public void update(WatchModel watchModel, boolean updateIncrementOnly) {
        database.getWritableDatabase().update(DatabaseKey.TABLE_WATCH, WatchMapper.contentValuesForWatch(watchModel, updateIncrementOnly), DatabaseKey.WATCH_COLUMN_ID + " = ? ", DatabaseUtils.asArgs(watchModel.getId()));
    }

    public void delete(int id) {
        database.getWritableDatabase().delete(DatabaseKey.TABLE_WATCH, DatabaseKey.WATCH_COLUMN_ID + "=?", DatabaseUtils.asArgs(id));
    }

    public List<WatchModel> getAll() {
        return getAll(Mode.UNARCHIVED);
    }

    public List<WatchModel> getAll(Mode mode) {
        List<WatchModel> watchModels = new ArrayList<>();
        Cursor cursor = database.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseKey.TABLE_WATCH + " WHERE " + DatabaseKey.WATCH_COLUMN_ARCHIVED + " = ? ", DatabaseUtils.asArgs(mode == Mode.ARCHIVED));
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            watchModels.add(WatchMapper.toWatchModel(cursor));
            cursor.moveToNext();
        }
        return watchModels;
    }
    //endregion

    //region enum
    public enum Mode{
        ARCHIVED,
        UNARCHIVED
    }
    //endregion
}
