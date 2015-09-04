package com.ombrax.watchers.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ombrax.watchers.Activities.ImageActivity;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Interfaces.Listener.IOnThumbnailImageSaveListener;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class WatchAddFragment extends Fragment implements IOnThumbnailImageSaveListener, View.OnClickListener {

    //region declaration
    //region bundle key
    private static final String MODEL_KEY = "_model";
    //endregion

    //region inner field
    private MenuController mc;
    private DomainController dc;
    private boolean editMode;
    //endregion

    //region variable
    private WatchModel watchModel;
    //endregion

    //region view
    private ImageView thumbnailImage;
    private EditText tvShowInput;
    private List<Button> toggleGroup;
    //endregion

    //endregion

    //region instance constructor
    public static WatchAddFragment editInstance(WatchModel watchModel) {
        WatchAddFragment instance = new WatchAddFragment();
        if (watchModel != null) {
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
        dc = DomainController.getInstance();
        dc.setOnThumbnailImageSaveListener(this);
        if (getArguments() != null) {
            watchModel = (WatchModel) getArguments().getSerializable(MODEL_KEY);
            editMode = (watchModel != null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        thumbnailImage = (ImageView) view.findViewById(R.id.fragment_add_thumbnail_image);
        tvShowInput = (EditText) view.findViewById(R.id.fragment_add_tv_show_input);

        getToggleGroup(view);
        viewSetup();

        if (editMode) {
            setModel();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(!editMode ? MenuItemType.ADD : MenuItemType.EDIT);
        mc.handleSecondaryMenuEnable(false);
    }
    //endregion

    //region helper
    private void getToggleGroup(View view) {
        toggleGroup = new ArrayList<>();
        ViewGroup container = (ViewGroup) view.findViewById(R.id.fragment_add_toggle_group_container);
        for (int i = 0; i < container.getChildCount(); i++) {
            Button toggle = (Button) container.getChildAt(i);
            toggle.setTag(ToggleOption.values()[i]);
            toggle.setOnClickListener(this);
            toggleGroup.add(toggle);
        }
    }

    private void viewSetup() {
        thumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ImageActivity.class));
            }
        });
        dynamicEditTextBackground();
        tvShowInput.clearFocus();
        toggleGroup.get(0).setSelected(true);
    }

    private void dynamicEditTextBackground() {
        tvShowInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                boolean isEmpty = tvShowInput.getText() == null ? true : tvShowInput.getText().toString().isEmpty();
                tvShowInput.setSelected(!hasFocus && !isEmpty);
            }
        });
    }

    private void setModel() {
        ImageUtils.loadImageFromFile(thumbnailImage, watchModel.getThumbnailPath());
        tvShowInput.setText(watchModel.getName());
        tvShowInput.requestFocus();
        tvShowInput.clearFocus();
        //TODO request focus on view
    }

    private void switchMode(ToggleOption toggleOption){
        switch (toggleOption){
            case OPTION_1:
                break;
            case OPTION_2:
                break;
            case OPTION_3:
                break;
        }
    }
    //endregion

    //region interface implementation
    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            Button selectedToggle = (Button) v;
            if (!selectedToggle.isSelected()) {
                for (Button toggle : toggleGroup) {
                    toggle.setSelected(false);
                }
                selectedToggle.setSelected(true);
                switchMode((ToggleOption) selectedToggle.getTag());
            }
            //TODO change layout according to option
        }
    }

    @Override
    public void onThumbnailImageSaved(String thumbnailImagePath) {
        ImageUtils.loadImageFromFile(thumbnailImage, thumbnailImagePath);
        //TEMP User must accept by clicking button on bottom of screen
        if (editMode) {
            watchModel.setThumbnailPath(thumbnailImagePath);
            WatchRepository.getInstance().update(watchModel, false);
        }
    }
    //endregion

    //region enum
    private enum ToggleOption {
        OPTION_1,
        OPTION_2,
        OPTION_3
    }
    //endregion
}
