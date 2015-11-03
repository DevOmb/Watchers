package com.ombrax.watchers.Fragments.PagerFragment;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devomb.cellgriddialog.CellGridDialogBuilder;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Other.NumericInputField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ombrax on 9/10/2015.
 */
public class WatchAddEntryMode2PagerFragment extends WatchAddEntryModePagerFragment {

    //region resource
    private int size50;
    private int defMargin;
    //endregion

    //region inner field
    private List<Integer> seasonEpisodeList = new ArrayList<>();
    //endregion

    //region view
    private NumericInputField seasonInput;
    private LinearLayout episodeListInputContainer;
    private TextView episodeListErrorLabel;
    //endregion

    //region init
    @Override
    protected void init(View view) {
        getViews(view);
        loadResources();
        setup();
    }

    private void loadResources() {
        size50 = getResources().getDimensionPixelSize(R.dimen.dp50);
        defMargin = getResources().getDimensionPixelOffset(R.dimen.default_1x);
    }

    private void setup() {
        //Model
        setModel();

        //Season
        registerKeyboardListener(seasonInput);
        seasonInput.setOnFocusChangeListener(new NumericInputField.OnFocusChangeListener() {
            @Override
            public void onFocusLost(EditText source, boolean hasValidInput, int value) {
                if (hasValidInput) {
                    invalidateEpisodeListInput(value);
                    model.setSeasonCount(value);
                    if (model.getCurrentSeason() > value) {
                        model.setCurrentSeason(value);
                        if (model.getCurrentEpisode() > seasonEpisodeList.get(value - 1)) {
                            model.setCurrentEpisode(seasonEpisodeList.get(value - 1));
                        }
                        updateStartAt();
                    }
                }
                setStartAtInputEnabled(hasValidInput && !isEpisodeListInputEmpty());
            }
        });

        //Start at
        startAtLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CellGridDialogBuilder.create(getChildFragmentManager(), model.getSeasonCount(), model.getSeasonEpisodeList())
                        .header("Season ", "Episodes")
                        .select(model.getCurrentSeason() - 1, model.getCurrentEpisode() - 1)
                        .onAcceptListener(WatchAddEntryMode2PagerFragment.this)
                        .show();
            }
        });
    }
    //endregion

    //region helper
    private void getViews(View view) {
        seasonInput = (NumericInputField) view.findViewById(R.id.fragment_add_season_input);
        episodeListInputContainer = (LinearLayout) view.findViewById(R.id.fragment_add_episode_list_input_container);
        episodeListErrorLabel = (TextView) view.findViewById(R.id.fragment_add_episode_list_error_label);
    }

    private boolean isEpisodeListInputEmpty() {
        return seasonEpisodeList.isEmpty() || seasonEpisodeList.contains(0);
    }

    public void invalidateEpisodeListInput(int newViewCount) {
        invalidateEpisodeListInput(newViewCount, true);
    }

    public void invalidateEpisodeListInput(int newViewCount, boolean editList) {
        //Calc difference in view count
        int viewCount = episodeListInputContainer.getChildCount();
        int delta = newViewCount - viewCount;

        if (delta != 0) {
            //Show inputs, hide error
            setEpisodeListInputEnabled(true);

            //Add or remove views to match the new view count
            if (delta < 0) {
                for (int i = viewCount - 1; i > newViewCount - 1; i--) {
                    episodeListInputContainer.removeViewAt(i);
                    if (editList) {
                        seasonEpisodeList.remove(i);
                    }
                }
            } else {
                for (int i = 0; i < delta; i++) {
                    episodeListInputContainer.addView(generateNewInputCircle());
                    if (editList) {
                        seasonEpisodeList.add(0);
                    }
                }
            }

            //Edit properties of all views
            NumericInputField last = null;
            for (int i = 0; i < newViewCount; i++) {
                last = (NumericInputField) episodeListInputContainer.getChildAt(i);
                last.clean();
                last.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }
            last.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        System.out.println("New List: " + seasonEpisodeList);
    }

    private NumericInputField generateNewInputCircle() {
        NumericInputField inputCircle = new NumericInputField(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size50, size50);
        params.setMargins(0, 0, defMargin, 0);
        inputCircle.setLayoutParams(params);
        registerKeyboardListener(inputCircle);
        inputCircle.setOnFocusChangeListener(new NumericInputField.OnFocusChangeListener() {
            @Override
            public void onFocusLost(EditText source, boolean hasValidInput, int value) {
                if (hasValidInput) {
                    int index = episodeListInputContainer.indexOfChild(source);
                    seasonEpisodeList.set(index, value);
                    if (!isEpisodeListInputEmpty() && model.getCurrentSeason() == index + 1 && model.getCurrentEpisode() > value) {
                        model.setCurrentEpisode(value);
                        updateStartAt();
                    }
                }
                setStartAtInputEnabled(hasValidInput && !isEpisodeListInputEmpty() && !seasonInput.isEmpty());
            }
        });
        return inputCircle;
    }

    private void setEpisodeListInputEnabled(boolean enabled) {
        episodeListInputContainer.setVisibility(enabled ? View.VISIBLE : View.GONE);
        episodeListErrorLabel.setVisibility(enabled ? View.GONE : View.GONE);
    }

    private void setModel() {
        setStartAtInputEnabled(model != null);
        if (model == null) {
            model = new WatchModel();
            model.setSeasonEpisodeList(seasonEpisodeList);
        } else {
            seasonInput.setText(String.valueOf(model.getSeasonCount()));
            if (model.getSeasonEpisodeList() != null) {
                seasonEpisodeList = model.getSeasonEpisodeList();
            }
            invalidateEpisodeListInput(model.getSeasonCount(), false);
            for (int i = 0; i < seasonEpisodeList.size(); i++) {
                NumericInputField input = (NumericInputField) episodeListInputContainer.getChildAt(i);
                if (input != null) {
                    input.setText(String.valueOf(seasonEpisodeList.get(i)));
                }
            }
        }
    }
    //endregion

    //region override
    @Override
    public String getStartAtErrorMessage() {
        if (isEpisodeListInputEmpty()) {
            if (seasonInput.isEmpty()) {
                return "Missing data: Seasons - Episode List";
            }
            return "Missing data: Episode List";
        }
        if (seasonInput.isEmpty()) {
            return "Missing data: Seasons";
        }
        return "";
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_pager_add_entry_mode_2;
    }
    //endregion
}
