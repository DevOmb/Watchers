package com.ombrax.watchers.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ombrax.watchers.Adapters.WatchListAdapter;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Interfaces.Command;
import com.ombrax.watchers.Interfaces.Observer.IOnUserSecondaryMenuCloseObserver;
import com.ombrax.watchers.Interfaces.Listener.IOnSortMenuItemChangeListener;
import com.ombrax.watchers.Manager.SortingManager;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;

import java.util.List;

/**
 * Created by Ombrax on 30/06/2015.
 */
public class WatchListFragment extends Fragment implements IOnSortMenuItemChangeListener, IOnUserSecondaryMenuCloseObserver {

    //region declaration

    //region protected field
    protected DomainController dc;
    protected MenuController mc;
    protected ToolbarManager toolbarManager;
    //endregion

    //region inner field
    private List<WatchModel> watchModels;

    private WatchListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    //endregion

    //endregion

    //region create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dc = DomainController.getInstance();
        mc = MenuController.getInstance();
        mc.setOnSortMenuItemChangeListener(this);
        toolbarManager = ToolbarManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.watch_list_recycler_view);

        adapter = new WatchListAdapter(getActivity(), watchModels);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.registerOnSecondaryMenuCloseObserver(this);
        mc.handleSecondaryMenuEnable(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mc.unregisterOnSecondaryMenuCloseObserver(this);
    }
    //endregion

    //region helper
    private void waitForGlobalLayout(final Command command) {
        getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getView() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    command.execute();
                }
            }
        });
    }
    //endregion

    //region protected method
    protected void setWatchModels(List<WatchModel> watchModels) {
        this.watchModels = watchModels;
    }

    protected void collapseToolbar(){
        toolbarManager.expandToolbar(false);
    }

    protected void expandToolbar() {
        waitForGlobalLayout(new Command() {
            @Override
            public void execute() {
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    toolbarManager.expandToolbar(true);
                }
            }
        });
    }

    protected void enableNestedScrolling(boolean enable){
        recyclerView.setNestedScrollingEnabled(enable);
    }
    //endregion

    //region interface implementation
    @Override
    public void onSortMenuItemChange(MenuItemType menuItemType, boolean isAscendingOrder) {
        adapter.sort(SortingManager.getInstance().get(menuItemType, isAscendingOrder));
    }

    @Override
    public void onUserSecondaryMenuClose() {
        //TODO get action from settings (sort)
        System.out.println("Secondary Menu Closed By User");
    }
    //endregion
}
