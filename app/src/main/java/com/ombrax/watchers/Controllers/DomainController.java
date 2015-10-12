package com.ombrax.watchers.Controllers;

import com.ombrax.watchers.Interfaces.Handler.IWatchCardAddFinishedHandler;
import com.ombrax.watchers.Interfaces.Handler.IWatchCardRemoveHandler;
import com.ombrax.watchers.Interfaces.Handler.IWatchCardUpdateHandler;
import com.ombrax.watchers.Interfaces.Listener.IOnThumbnailImageSaveListener;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.Interfaces.Listener.IOnListItemEditListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ombrax on 30/05/2015.
 */
public class DomainController {

    //region singleton
    private static DomainController instance = new DomainController();

    private DomainController() {
    }

    public static DomainController getInstance() {
        return instance;
    }
    //endregion

    //region observer
    private List<IOnListItemEditListener<WatchModel>> onListItemEditListeners = new ArrayList<>();

    public void registerOnListItemEditObserver(IOnListItemEditListener onListItemEditListener) {
        if (!onListItemEditListeners.contains(onListItemEditListener)) {
            onListItemEditListeners.add(onListItemEditListener);
        }
    }

    public void unregisterOnListItemEditObserver(IOnListItemEditListener onListItemEditListener) {
        onListItemEditListeners.remove(onListItemEditListener);
    }

    public void notifyListItemObservers(WatchModel watchModel) {
        for (IOnListItemEditListener listener : onListItemEditListeners) {
            listener.onListItemEdit(watchModel);
        }
    }
    //endregion

    //region listener
    private IOnThumbnailImageSaveListener onThumbnailImageSaveListener;

    public void setOnThumbnailImageSaveListener(IOnThumbnailImageSaveListener onThumbnailImageSaveListener) {
        this.onThumbnailImageSaveListener = onThumbnailImageSaveListener;
    }

    public void onThumbnailImageSaved(String path) {
        if (onThumbnailImageSaveListener != null) {
            onThumbnailImageSaveListener.onThumbnailImageSaved(path);
        }
    }
    //endregion

    //region handler
    private IWatchCardUpdateHandler watchCardUpdateHandler;
    private IWatchCardRemoveHandler watchCardRemoveHandler;
    private IWatchCardAddFinishedHandler watchCardAddFinishedHandler;

    public void setWatchCardUpdateHandler(IWatchCardUpdateHandler watchCardUpdateHandler) {
        this.watchCardUpdateHandler = watchCardUpdateHandler;
    }

    public void setWatchCardRemoveHandler(IWatchCardRemoveHandler watchCardRemoveHandler) {
        this.watchCardRemoveHandler = watchCardRemoveHandler;
    }

    public void setWatchCardAddFinishedHandler(IWatchCardAddFinishedHandler watchCardAddFinishedHandler) {
        this.watchCardAddFinishedHandler = watchCardAddFinishedHandler;
    }

    public void handleWatchCardUpdate() {
        if (watchCardUpdateHandler != null) {
            watchCardUpdateHandler.handleWatchCardUpdate();
        }
    }

    public void handleWatchCardRemove(WatchModel watchModel) {
        if (watchCardRemoveHandler != null) {
            watchCardRemoveHandler.handleWatchCardRemove(watchModel);
        }
    }

    public void handleWatchCardAddFinished(){
        if(watchCardAddFinishedHandler != null){
            watchCardAddFinishedHandler.handleWatchCardAddFinished();
        }
    }
    //endregion
}
