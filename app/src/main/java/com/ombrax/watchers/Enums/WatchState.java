package com.ombrax.watchers.Enums;

/**
 * Created by Ombrax on 2/08/2015.
 */
public enum WatchState {

    //region enum
    FIRST_EPISODE,
    DEFAULT,
    SEASON_FINALE("Season Finale"),
    LAST_EPISODE("Final Episode");
    //endregion

    //region constant
    private final String DEFAULT_TEXT = "Next up";
    //endregion

    //region variable
    private String text;
    //endregion

    //region constructor
    WatchState(String text) {
        this.text = text;
    }

    WatchState() {
        this.text = DEFAULT_TEXT;
    }
    //endregion

    //region toString
    @Override
    public String toString() {
        return text;
    }
    //endregion
}
