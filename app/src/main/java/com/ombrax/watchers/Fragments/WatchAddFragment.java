package com.ombrax.watchers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ombrax.watchers.Activities.ImageActivity;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Fragments.PagerFragment.WatchAddEntryMode1PagerFragment;
import com.ombrax.watchers.Fragments.PagerFragment.WatchAddEntryMode2PagerFragment;
import com.ombrax.watchers.Fragments.PagerFragment.WatchAddEntryMode3PagerFragment;
import com.ombrax.watchers.Fragments.PagerFragment.WatchAddEntryModePagerFragment;
import com.ombrax.watchers.Helper.BootstrapToggleHandler;
import com.ombrax.watchers.Interfaces.Handler.IMenuStateHandler;
import com.ombrax.watchers.Interfaces.Listener.IOnThumbnailImageSaveListener;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.DialogUtils;
import com.ombrax.watchers.Utils.ImageUtils;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Utils.StorageUtils;
import com.ombrax.watchers.Views.Other.ImmutableViewPager;

public class WatchAddFragment extends Fragment implements IOnThumbnailImageSaveListener, BootstrapToggleHandler.OnToggleSelectListener<WatchAddFragment.EntryMode> {

    //region declaration
    //region bundle key
    private static final String MODEL_KEY = "_model";
    //endregion

    //region controller and manager
    private MenuController mc;
    private DomainController dc;
    private WatchRepository repo;
    private ToolbarManager toolbarManager;
    //endregion

    //region delegate handler
    private BootstrapToggleHandler<EntryMode> bootstrapToggleHandler;
    //endregion

    //region view
    private ImageView thumbnailImage;
    private EditText tvShowInput;
    private LinearLayout toggleGroupContainer;
    private ImmutableViewPager propertyInputContainer;
    private FragmentPagerAdapter propertyInputAdapter;
    private Button saveButton;
    private Button cancelButton;
    //endregion

    //region model
    private WatchModel watchModel;
    private boolean editMode;
    private String thumbnailPath;
    private String replacedThumbnailPath;
    private boolean saved;
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
        repo = WatchRepository.getInstance();
        toolbarManager = ToolbarManager.getInstance();
        if (getArguments() != null) {
            watchModel = (WatchModel) getArguments().getSerializable(MODEL_KEY);
            editMode = (watchModel != null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        getViews(view);
        init();
        setModel(editMode);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(!editMode ? MenuItemType.ADD : MenuItemType.EDIT);
        mc.setMenuFlags(IMenuStateHandler.DISABLE_SECONDARY_MENU | IMenuStateHandler.DISABLE_SWIPE);
        toolbarManager.setExpandingTitleOnTransition((!editMode ? "New" : "Edit"), false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!saved && thumbnailPath != null){
            StorageUtils.removeFromStorage(thumbnailPath);
        }
    }
    //endregion

    //region setup
    private void init() {
        //Thumbnail
        thumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ImageActivity.class));
            }
        });

        //TvShow
        tvShowInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String trimmedText = tvShowInput.getText().toString().trim();
                tvShowInput.setText(trimmedText);
                tvShowInput.setSelected(!hasFocus && !trimmedText.isEmpty());
            }
        });
        tvShowInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LayoutUtils.clearFocusOnKeyboardHide(propertyInputContainer);
                }
                return false;
            }
        });

        //Bootstrap Toggles
        bootstrapToggleHandler = new BootstrapToggleHandler<>(EntryMode.class, toggleGroupContainer);
        bootstrapToggleHandler.setOnToggleSelectListener(this);

        //Property Content
        propertyInputAdapter = new EntryModeFragmentPagerAdapter(getChildFragmentManager(), watchModel);
        propertyInputContainer.setAdapter(propertyInputAdapter);
        propertyInputContainer.setOffscreenPageLimit(2);
        //TODO Disable swipe (override touch)

        //Save & Cancel
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validForSave()) {
                    saved = true;
                    watchModel = getEntryModeFragment().getModel();
                    if(thumbnailPath != null) {
                        watchModel.setThumbnailPath(thumbnailPath);
                    }
                    watchModel.setName(tvShowInput.getText().toString());
                    if (!editMode) {
                        repo.insert(watchModel);
                    }else{
                        if(replacedThumbnailPath != null){
                            StorageUtils.removeFromStorage(replacedThumbnailPath);
                        }
                        repo.update(watchModel, false);
                    }
                    dc.handleWatchCardAddFinished();
                } else {
                    StringBuilder error = new StringBuilder();
                    if (tvShowInput.getText().toString().trim().isEmpty()) {
                        error.append("Tv Show").append("\n");
                    }
                    String propertiesError = getEntryModeFragment().getStartAtErrorMessage();
                    if (!propertiesError.isEmpty()) {
                        String missingProperties = propertiesError.split(":")[1].trim();
                        String[] missingElements = missingProperties.split("-");
                        for (String el : missingElements) {
                            error.append(el.trim()).append("\n");
                        }
                    }
                    DialogUtils.newMissingInputDialog(getActivity(), error.toString()).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.handleWatchCardAddFinished();
            }
        });
    }
    //endregion

    //region helper
    private void getViews(View view) {
        thumbnailImage = (ImageView) view.findViewById(R.id.fragment_add_thumbnail_image);
        tvShowInput = (EditText) view.findViewById(R.id.fragment_add_tv_show_input);
        toggleGroupContainer = (LinearLayout) view.findViewById(R.id.fragment_add_toggle_group_container);
        propertyInputContainer = (ImmutableViewPager) view.findViewById(R.id.fragment_add_property_input_container);
        saveButton = (Button) view.findViewById(R.id.fragment_add_save);
        cancelButton = (Button) view.findViewById(R.id.fragment_add_cancel);
    }

    private void setModel(boolean editMode) {
        if (!editMode)
            return;

        ImageUtils.loadImageFromFile(thumbnailImage, watchModel.getThumbnailPath());
        tvShowInput.setText(watchModel.getName());
        bootstrapToggleHandler.selectAndDisable(getToggleIndexForModel(watchModel));
    }

    private int getToggleIndexForModel(WatchModel watchModel){
        if(watchModel.hasEpisodesOnly()){
            return 2;
        }
        if(watchModel.getSeasonEpisodeList() == null){
            return 0;
        }
        return 1;
    }

    private boolean validForSave() {
        return getEntryModeFragment().getStartAtErrorMessage().isEmpty() && !tvShowInput.getText().toString().trim().isEmpty();
    }

    private WatchAddEntryModePagerFragment getEntryModeFragment() {
        return (WatchAddEntryModePagerFragment) propertyInputAdapter.getItem(propertyInputContainer.getCurrentItem());
    }
    //endregion

    //region interface implementation
    @Override
    public void onToggleSelect(Button selectedToggle, EntryMode selectedEnum) {
        propertyInputContainer.setCurrentItem(selectedEnum.ordinal(), false);
    }

    @Override
    public void onThumbnailImageSaved(String thumbnailImagePath) {
        ImageUtils.loadImageFromFile(thumbnailImage, thumbnailImagePath);
        thumbnailPath = thumbnailImagePath;
        if(editMode){
            replacedThumbnailPath = watchModel.getThumbnailPath();
        }
    }
    //endregion

    //region inner class
    //region adapter
    private class EntryModeFragmentPagerAdapter extends FragmentPagerAdapter {

        private WatchAddEntryModePagerFragment f1;
        private WatchAddEntryModePagerFragment f2;
        private WatchAddEntryModePagerFragment f3;

        public EntryModeFragmentPagerAdapter(FragmentManager fm, WatchModel model) {
            super(fm);
            init(model);
        }

        private void init(WatchModel model) {
            f1 = new WatchAddEntryMode1PagerFragment();
            f1.setModel(model);
            f2 = new WatchAddEntryMode2PagerFragment();
            f2.setModel(model);
            f3 = new WatchAddEntryMode3PagerFragment();
            f3.setModel(model);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return f1;
                case 1:
                    return f2;
                case 2:
                    return f3;
                default:
                    return new android.support.v4.app.Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    //endregion

    //region enum
    public enum EntryMode {
        OPTION_1("Standard"),
        OPTION_2("Sequence"),
        OPTION_3("Singular");

        //region custom
        private String display;

        EntryMode(String display) {
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
        //endregion
    }
    //endregion
    //endregion
}


