package com.ombrax.watchers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ombrax.watchers.Activities.ImageActivity;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Interfaces.Listener.IOnThumbnailImageSaveListener;
import com.ombrax.watchers.Manager.ToolbarManager;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.ImageUtils;
import com.ombrax.watchers.Views.Button.NumericInputCircle;

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
    private WatchRepository repo;
    private ToolbarManager toolbarManager;

    private boolean editMode;
    private int dp50;
    private int defaultMargin;

    private ToggleOption selectedToggleOption;
    private int episodeListViewCount;
    //endregion

    //region variable
    private WatchModel watchModel;
    //endregion

    //region view
    private ImageView thumbnailImage;
    private EditText tvShowInput;
    private List<Button> toggleGroup;
    private Button selectedToggle;
    private LinearLayout seasonContainer;
    private LinearLayout episodeContainer;
    private RelativeLayout episodeListContainer;
    private NumericInputCircle seasonInput;
    private NumericInputCircle episodeInput;
    private LinearLayout episodeList;
    private TextView episodeListEmptyLabel;
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
        loadResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        thumbnailImage = (ImageView) view.findViewById(R.id.fragment_add_thumbnail_image);
        tvShowInput = (EditText) view.findViewById(R.id.fragment_add_tv_show_input);
        seasonContainer = (LinearLayout) view.findViewById(R.id.fragment_add_season_input_container);
        episodeContainer = (LinearLayout) view.findViewById(R.id.fragment_add_episode_input_container);
        episodeListContainer = (RelativeLayout) view.findViewById(R.id.fragment_add_episode_list_input_container);
        seasonInput = (NumericInputCircle) view.findViewById(R.id.fragment_add_season_input);
        episodeInput = (NumericInputCircle) view.findViewById(R.id.fragment_add_episode_input);
        episodeList = (LinearLayout) view.findViewById(R.id.fragment_add_episode_list);
        episodeListEmptyLabel = (TextView) view.findViewById(R.id.fragment_add_episode_list_empty_label);

        initialSetup(view);

        if (editMode) {
            setModel();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mc.onMenuItemSelect(!editMode ? MenuItemType.ADD : MenuItemType.EDIT);
        mc.handleSortMenuEnable(false);
        toolbarManager.setToolbarExpanded(false);
        toolbarManager.setExpandingTitle(!editMode ? "New" : "Edit");
    }
    //endregion

    //region setup
    private void loadResources() {
        dp50 = getResources().getDimensionPixelSize(R.dimen.dp50);
        defaultMargin = getResources().getDimensionPixelSize(R.dimen.default_1x);
    }

    private void initialSetup(View view) {
        thumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ImageActivity.class));
            }
        });
        setupToggleGroup(view);
        setupDynamicEditText();
        tvShowInput.clearFocus();
        episodeInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        seasonInput.setOnInputListener(new NumericInputCircle.OnInputListener() {
            @Override
            public void onValidInput(int input) {
                episodeListViewCount = input;
                if (selectedToggleOption == ToggleOption.OPTION_2) {
                    setupEpisodeList(input);
                }
            }

            @Override
            public void onInvalidInput() {
                episodeListEmptyLabel.setVisibility(View.VISIBLE);
                episodeList.setVisibility(View.GONE);
                seasonInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
        });
    }
    //endregion

    //region helper

    //region individual setup
    private void setupToggleGroup(View view) {
        toggleGroup = new ArrayList<>();
        ViewGroup container = (ViewGroup) view.findViewById(R.id.fragment_add_toggle_group_container);
        for (int i = 0; i < container.getChildCount(); i++) {
            Button toggle = (Button) container.getChildAt(i);
            toggle.setTag(ToggleOption.values()[i]);
            toggle.setOnClickListener(this);
            toggleGroup.add(toggle);
        }
        toggleGroup.get(0).performClick();
    }

    private void setupDynamicEditText() {
        tvShowInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                boolean isEmpty = tvShowInput.getText() == null ? true : tvShowInput.getText().toString().isEmpty();
                tvShowInput.setSelected(!hasFocus && !isEmpty);
            }
        });
    }

    private void setupEpisodeList(int newViewCount) {
        episodeList.setVisibility(View.VISIBLE);
        episodeListEmptyLabel.setVisibility(View.GONE);

        //View
        int viewCount = episodeList.getChildCount();
        int delta = newViewCount - viewCount;
        if (delta == 0) {
            return;
        } else if (delta < 0) {
            for (int i = viewCount - 1; i > newViewCount - 1; i--) {
                episodeList.removeViewAt(i);
            }
        } else {
            for (int i = 0; i < delta; i++) {
                episodeList.addView(generateNewInputCircle());
            }
        }

        //Refresh
        NumericInputCircle last = null;
        for (int i = 0; i < newViewCount; i++) {
            last = (NumericInputCircle) episodeList.getChildAt(i);
            last.reset();
            last.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
        last.setImeOptions(EditorInfo.IME_ACTION_DONE);

    }

    private NumericInputCircle generateNewInputCircle() {
        NumericInputCircle inputCircle = new NumericInputCircle(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp50, dp50);
        params.setMargins(0, 0, defaultMargin, 0);
        inputCircle.setLayoutParams(params);
        inputCircle.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        return inputCircle;
    }
    //endregion

    private void setModel() {
        ImageUtils.loadImageFromFile(thumbnailImage, watchModel.getThumbnailPath());
        tvShowInput.setText(watchModel.getName());
        tvShowInput.requestFocus();
        tvShowInput.clearFocus();
    }

    private void switchMode() {
        hideKeyboard();
        switch (selectedToggleOption) {
            case OPTION_1:
                seasonInput.reset();
                seasonInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                episodeInput.reset();
                break;
            case OPTION_2:
                seasonInput.reset();
                seasonInput.setImeOptions(episodeList.getVisibility() == View.VISIBLE ? EditorInfo.IME_ACTION_NEXT : EditorInfo.IME_ACTION_DONE);
                if (episodeList.getChildCount() != episodeListViewCount) {
                    setupEpisodeList(episodeListViewCount);
                }
                break;
            case OPTION_3:
                episodeInput.reset();
                break;
        }
        seasonContainer.setVisibility(selectedToggleOption == ToggleOption.OPTION_3 ? View.GONE : View.VISIBLE);
        episodeContainer.setVisibility(selectedToggleOption == ToggleOption.OPTION_2 ? View.GONE : View.VISIBLE);
        episodeListContainer.setVisibility(selectedToggleOption == ToggleOption.OPTION_2 ? View.VISIBLE : View.GONE);
    }

    private void hideKeyboard(){
        View focusView = getActivity().getCurrentFocus();
        if(focusView != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            focusView.clearFocus();
        }
    }
    //endregion

    //region interface implementation
    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button toggle = (Button) v;
            if (toggle != selectedToggle) {
                if (selectedToggle != null) {
                    selectedToggle.setSelected(false);
                }
                selectedToggle = toggle;
                selectedToggle.setSelected(true);
                selectedToggleOption = (ToggleOption) toggle.getTag();
                switchMode();
            }
        }
    }

    @Override
    public void onThumbnailImageSaved(String thumbnailImagePath) {
        ImageUtils.loadImageFromFile(thumbnailImage, thumbnailImagePath);
        //TEMP User must accept by clicking button on bottom of screen
        if (editMode) {
            watchModel.setThumbnailPath(thumbnailImagePath);
            repo.update(watchModel, false);
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
