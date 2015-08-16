package com.ombrax.watchers.Controllers;

import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.Interfaces.IOnListItemEditListener;

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

    //region observers
    private List<IOnListItemEditListener<WatchModel>> onListItemEditListeners = new ArrayList<>();
    //endregion

    //region register
    public void registerOnListItemEditObserver(IOnListItemEditListener onListItemEditListener){
        if(!onListItemEditListeners.contains(onListItemEditListener)){
            onListItemEditListeners.add(onListItemEditListener);
        }
    }
    //endregion

    //region unregister
    public void unregisterOnListItemEditObserver(IOnListItemEditListener onListItemEditListener){
        onListItemEditListeners.remove(onListItemEditListener);
    }
    //endregion

    //region notify
    public void notifyListItemObservers(WatchModel watchModel){
        for(IOnListItemEditListener listener : onListItemEditListeners){
            listener.onListItemEdit(watchModel);
        }
    }
    //endregion

    //endregion

}
