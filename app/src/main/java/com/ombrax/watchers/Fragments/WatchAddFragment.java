package com.ombrax.watchers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;

public class WatchAddFragment extends Fragment {

    //region declaration
    //region bundle key
    private static final String MODEL_KEY = "_model";
    //endregion

    //region inner field
    private MenuController mc;
    private boolean editMode;
    //endregion

    //region variable
    private WatchModel watchModel;
    //endregion
    //endregion

    //region instance constructor
    public static WatchAddFragment editInstance(WatchModel watchModel) {
        WatchAddFragment instance = new WatchAddFragment();
        if(watchModel != null){
            Bundle args = new Bundle();
            args.putSerializable(MODEL_KEY, watchModel);
            instance.setArguments(args);
        }
        return instance;
    }
    //endregion

    //region create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mc = MenuController.getInstance();
        if(getArguments() != null){
            watchModel = (WatchModel) getArguments().getSerializable(MODEL_KEY);
            editMode = (watchModel != null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        TextView txt = (TextView) view.findViewById(R.id.add_or_edit);

        txt.setText(!editMode ? "ADD Fragment" : "EDIT Fragment");

        if(editMode){
            setModel();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(!editMode ? MenuItemType.ADD : MenuItemType.EDIT);
    }
    //endregion

    private void setModel(){

    }
}
