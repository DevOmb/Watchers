package com.ombrax.watchers.Models;

import android.os.Parcelable;

import com.ombrax.watchers.Enums.WatchState;
import com.ombrax.watchers.Utils.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ombrax on 23/06/2015.
 */
public class WatchModel implements Serializable {

    //region declaration

    //region constant
    private static final String EPISODE_PREFIX = "E";
    private static final String SEASON_PREFIX = "S";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    //endregion

    //region inner field
    private WatchState watchState;
    private int totalEpisodeCount;
    private int episodeProgress;
    //endregion

    //region variable
    private int id;
    private String name;
    private String thumbnailPath;
    private int seasonCount;
    private int seasonEpisodeCount;
    private List<Integer> seasonEpisodeList;
    private int currentSeason;
    private int currentEpisode;
    private boolean episodesOnly;
    private Date lastViewed;
    private boolean archived;
    private boolean completed;
    //endregion

    //endregion

    //region constructor
    public WatchModel(int id, String name, String thumbnailPath, int seasonCount, int seasonEpisodeCount, List<Integer> seasonEpisodeList, int currentSeason, int currentEpisode, boolean episodesOnly, Date lastViewed, boolean archived, boolean completed) {
        this.id = id;
        this.name = name;
        this.thumbnailPath = thumbnailPath;
        this.seasonCount = seasonCount;
        this.seasonEpisodeCount = seasonEpisodeCount;
        this.seasonEpisodeList = seasonEpisodeList;
        this.currentSeason = currentSeason;
        this.currentEpisode = currentEpisode;
        this.episodesOnly = episodesOnly;
        this.lastViewed = lastViewed;
        this.archived = archived;
        this.completed = completed;

        setup();
    }
    //endregion

    //region setup
    private void setup() {
        if (episodesOnly) {
            //Get State for entry with only episodes
            if (currentEpisode == 1) {
                watchState = WatchState.FIRST_EPISODE;
            } else if (currentEpisode == seasonEpisodeCount) {
                watchState = WatchState.LAST_EPISODE;
            } else {
                watchState = WatchState.DEFAULT;
            }

            //Calc progress resources
            totalEpisodeCount = seasonEpisodeCount;
            episodeProgress = currentEpisode;
        } else {
            if (seasonEpisodeList != null) {
                //Get State for entry with variable episodes per season
                if (currentSeason == 1 && currentEpisode == 1) {
                    watchState = WatchState.FIRST_EPISODE;
                } else if (currentEpisode == seasonEpisodeList.get(currentSeason - 1)) {
                    watchState = WatchState.SEASON_FINALE;
                    if (currentSeason == seasonCount) {
                        watchState = WatchState.LAST_EPISODE;
                    }
                } else {
                    watchState = WatchState.DEFAULT;
                }

                //Calc progress resources
                for (Integer seasonEpisodes : seasonEpisodeList) {
                    totalEpisodeCount += seasonEpisodes;
                }
                for (int i = 0; i < currentSeason - 1; i++) {
                    episodeProgress += seasonEpisodeList.get(i);
                }
                episodeProgress += currentEpisode;
            } else {
                //Get State for entry with consistent episodes per season
                if (currentSeason == 1 && currentEpisode == 1) {
                    watchState = WatchState.FIRST_EPISODE;
                } else if (currentEpisode == seasonEpisodeCount) {
                    watchState = WatchState.SEASON_FINALE;
                    if (currentSeason == seasonCount) {
                        watchState = WatchState.LAST_EPISODE;
                    }
                } else {
                    watchState = WatchState.DEFAULT;
                }

                //Calc progress resources
                totalEpisodeCount = seasonEpisodeCount * seasonCount;
                episodeProgress = ((currentSeason - 1) * seasonEpisodeCount) + currentEpisode;
            }
        }
    }
    //endregion

    //region getter setter

    //region getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public int getSeasonEpisodeCount() {
        return seasonEpisodeCount;
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public List<Integer> getSeasonEpisodeList() {
        return seasonEpisodeList;
    }

    public boolean hasEpisodesOnly() {
        return episodesOnly;
    }

    public Date getLastViewed() {
        return lastViewed;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean nowWatching() {
        return lastViewed != null;
    }

    public WatchState getWatchState() {
        return watchState;
    }

    public Float getProgress() {
        return ((float) episodeProgress / totalEpisodeCount);
    }

    public Integer getTotalEpisodeCount() {
        return totalEpisodeCount;
    }
    //endregion

    //region setter
    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public void setSeasonEpisodeCount(int seasonEpisodeCount) {
        this.seasonEpisodeCount = seasonEpisodeCount;
    }

    public void setCurrentSeason(int currentSeason) {
        this.currentSeason = currentSeason;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public void setSeasonEpisodeList(List<Integer> seasonEpisodeList) {
        this.seasonEpisodeList = seasonEpisodeList;
    }

    public void setEpisodesOnly(boolean episodesOnly) {
        this.episodesOnly = episodesOnly;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    //endregion

    //endregion

    //region method
    public boolean increment() {

        switch (watchState) {
            case LAST_EPISODE:
                return false;
            case SEASON_FINALE:
                watchState = WatchState.DEFAULT;
                currentSeason++;
                currentEpisode = 1;
                break;
            case FIRST_EPISODE:
                watchState = WatchState.DEFAULT;
            case DEFAULT:
                currentEpisode++;
                break;
        }

        if (currentEpisode == (seasonEpisodeList == null ? seasonEpisodeCount : seasonEpisodeList.get(currentSeason - 1))) {
            if (episodesOnly || currentSeason == seasonCount) {
                watchState = WatchState.LAST_EPISODE;
            } else {
                watchState = WatchState.SEASON_FINALE;
            }
        }
        episodeProgress++;
        renewLastViewed();
        return true;
    }

    public boolean decrement() {

        switch (watchState) {
            case FIRST_EPISODE:
                return false;
            case LAST_EPISODE:
            case SEASON_FINALE:
            case DEFAULT:
                //2nd to 1st Episode
                if (currentEpisode == 2 && (currentSeason == 1 || episodesOnly)) {
                    currentEpisode--;
                    watchState = WatchState.FIRST_EPISODE;
                    resetLastViewed();
                    break;
                }
                //1st of Season to previous Season Finale
                if (currentEpisode == 1) {
                    currentSeason--;
                    currentEpisode = seasonEpisodeList == null ? seasonEpisodeCount : seasonEpisodeList.get(currentSeason - 1);
                    watchState = WatchState.SEASON_FINALE;
                    renewLastViewed();
                }
                //Default
                else {
                    currentEpisode--;
                    watchState = WatchState.DEFAULT;
                    renewLastViewed();
                }
                break;
        }
        episodeProgress--;
        return true;
    }

    public String format(Format watchFormat) {
        switch (watchFormat) {
            case WATCH:
                String eString = StringUtils.preZeroInt(currentEpisode);
                if (episodesOnly) {
                    return EPISODE_PREFIX + eString;
                }
                String sString = StringUtils.preZeroInt(currentSeason);
                return SEASON_PREFIX + sString + EPISODE_PREFIX + eString;
            case DATE:
                return DATE_FORMAT.format(lastViewed);
        }
        return null;
    }
    //endregion

    //region helper
    private void resetLastViewed() {
        lastViewed = null;
    }

    private void renewLastViewed() {
        lastViewed = new Date();
    }
    //endregion

    //region enum
    public enum Format {
        WATCH,
        DATE
    }
    //endregion
}
