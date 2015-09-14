package com.ombrax.watchers.Database;

import android.content.ContentValues;
import android.database.Cursor;

import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.Utils.DatabaseUtils;

import java.util.Date;
import java.util.List;

public class WatchMapper {

    //region model
    public static WatchModel toWatchModel(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_NAME));
        String thumbnail = cursor.getString(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_THUMBNAIL));
        int seasonCount = cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_SEASON_COUNT));
        int seasonEpisodeCount = cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_SEASON_EPISODE_COUNT));
        List<Integer> seasonEpisodeList = DatabaseUtils.formatStringToIntList(cursor.getString(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_SEASON_EPISODE_LIST)));
        int currentSeason = cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_CURRENT_SEASON));
        int currentEpisode = cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_CURRENT_EPISODE));
        boolean episodesOnly = DatabaseUtils.formatIntegerToBoolean(cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_EPISODES_ONLY)));
        Date lastViewed = DatabaseUtils.formatStringToDate(cursor.getString(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_LAST_VIEWED)));
        boolean archived = DatabaseUtils.formatIntegerToBoolean(cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_ARCHIVED)));
        boolean completed = DatabaseUtils.formatIntegerToBoolean(cursor.getInt(cursor.getColumnIndex(DatabaseKey.WATCH_COLUMN_COMPLETED)));
        return new WatchModel(id, name, thumbnail, seasonCount, seasonEpisodeCount, seasonEpisodeList, currentSeason, currentEpisode, episodesOnly, lastViewed, archived, completed);
    }
    //endregion

    //region content values
    public static ContentValues contentValuesForWatch(WatchModel watchModel) {
        return contentValuesForWatch(watchModel, false);
    }

    public static ContentValues contentValuesForWatch(WatchModel watchModel, boolean updateIncrementOnly) {

        ContentValues values = new ContentValues();
        values.put(DatabaseKey.WATCH_COLUMN_CURRENT_EPISODE, watchModel.getCurrentEpisode());
        values.put(DatabaseKey.WATCH_COLUMN_CURRENT_SEASON, watchModel.getCurrentSeason());
        values.put(DatabaseKey.WATCH_COLUMN_LAST_VIEWED, DatabaseUtils.formatDateToString(watchModel.getLastViewed()));
        if(updateIncrementOnly){
            return values;
        }
        values.put(DatabaseKey.WATCH_COLUMN_NAME, watchModel.getName());
        values.put(DatabaseKey.WATCH_COLUMN_THUMBNAIL, watchModel.getThumbnailPath());
        values.put(DatabaseKey.WATCH_COLUMN_SEASON_COUNT, watchModel.getSeasonCount());
        values.put(DatabaseKey.WATCH_COLUMN_SEASON_EPISODE_COUNT, watchModel.getSeasonEpisodeCount());
        values.put(DatabaseKey.WATCH_COLUMN_SEASON_EPISODE_LIST, DatabaseUtils.formatIntListToString(watchModel.getSeasonEpisodeList()));
        values.put(DatabaseKey.WATCH_COLUMN_EPISODES_ONLY, DatabaseUtils.formatBooleanToInteger(watchModel.hasEpisodesOnly()));
        values.put(DatabaseKey.WATCH_COLUMN_LAST_VIEWED, DatabaseUtils.formatDateToString(watchModel.getLastViewed()));
        values.put(DatabaseKey.WATCH_COLUMN_ARCHIVED, DatabaseUtils.formatBooleanToInteger(watchModel.isArchived()));
        values.put(DatabaseKey.WATCH_COLUMN_COMPLETED, DatabaseUtils.formatBooleanToInteger(watchModel.isCompleted()));
        return values;
    }
    //endregion
}
