package com.ombrax.watchers.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ombrax.watchers.Adapters.WatchListAdapter;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Interfaces.Listener.IOnSortOrderChangeListener;
import com.ombrax.watchers.Manager.SortingManager;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.Models.SortModel;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;

import java.util.List;

/**
 * Created by Ombrax on 30/06/2015.
 */
public class WatchListFragment extends Fragment implements IOnSortOrderChangeListener{

    //region inner field
    protected DomainController dc;
    protected MenuController mc;
    protected ToolbarManager toolbarManager;
    protected SortingManager sortingManager;

    protected List<WatchModel> watchModels;
    protected WatchListAdapter adapter;
    protected LinearLayoutManager layoutManager;
    protected RecyclerView recyclerView;
    //endregion

    //region create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dc = DomainController.getInstance();
        mc = MenuController.getInstance();
        mc.setOnSortOrderChangeListener(this);
        toolbarManager = ToolbarManager.getInstance();
        sortingManager = SortingManager.getInstance();
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
        mc.handleSortMenuEnable(true);
        adapter.sort(sortingManager.getCurrentComparator());
    }
    //endregion

    //region interface implementation
    @Override
    public void onSortOrderChange(SortModel sortModel) {
        adapter.sort(sortingManager.getComparator(sortModel));
    }
    //endregion
}
