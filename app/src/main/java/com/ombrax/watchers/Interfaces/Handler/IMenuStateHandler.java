package com.ombrax.watchers.Interfaces.Handler;

/**
 * Created by Ombrax on 23/08/2015.
 */
public interface IMenuStateHandler {
    int DISABLE_SECONDARY_MENU = 1;
    int DISABLE_SWIPE = 2;
    int ENABLE_SWIPE = 4;
    int ENABLE_ALL = 8;

    void setMenuFlags(int flags);

}
