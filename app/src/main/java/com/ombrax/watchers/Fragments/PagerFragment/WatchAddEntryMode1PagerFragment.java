package com.ombrax.watchers.Fragments.PagerFragment;

import android.view.View;
import android.widget.EditText;

import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Button.NumericInputField;

/**
 * Created by Ombrax on 9/10/2015.
 */
public class WatchAddEntryMode1PagerFragment extends WatchAddEntryModePagerFragment {

    //region view
    private NumericInputField seasonInput;
    private NumericInputField episodeInput;
    //endregion

    //region init
    @Override
    protected void init(View view) {
        getViews(view);
        setup();
    }

    private void setup() {
        //Model
        setModel();

        //Season
        registerKeyboardListener(seasonInput);
        seasonInput.setOnFocusChangeListener(new NumericInputField.OnFocusChangeListener() {
            @Override
            public void onFocusLost(EditText source, boolean hasValidInput, int value) {
                setStartAtInputEnabled(hasValidInput && !episodeInput.isEmpty());
                if (hasValidInput) {
                    model.setSeasonCount(value);
                    if(model.getCurrentSeason() > value) {
                        model.setCurrentSeason(value);
                        updateStartAt();
                    }
                }
            }
        });

        //Episode
        registerKeyboardListener(episodeInput);
        episodeInput.setOnFocusChangeListener(new NumericInputField.OnFocusChangeListener() {
            @Override
            public void onFocusLost(EditText source, boolean hasValidInput, int value) {
                setStartAtInputEnabled(hasValidInput && !seasonInput.isEmpty());
                if (hasValidInput) {
                    model.setSeasonEpisodeCount(value);
                    if (model.getCurrentEpisode() > value) {
                        model.setCurrentEpisode(value);
                        updateStartAt();
                    }
                }
            }
        });
    }
    //endregion

    //region helper
    private void getViews(View view) {
        seasonInput = (NumericInputField) view.findViewById(R.id.fragment_add_season_input);
        episodeInput = (NumericInputField) view.findViewById(R.id.fragment_add_episode_input);
    }

    private void setModel() {
        setStartAtInputEnabled(model != null);
        if (model == null) {
            model = new WatchModel();
            model.setCurrentSeason(1);
            model.setCurrentEpisode(1);
        } else {
            seasonInput.setText(String.valueOf(model.getSeasonCount()));
            episodeInput.setText(String.valueOf(model.getSeasonEpisodeCount()));
        }
    }
    //endregion

    //region override
    @Override
    public String getStartAtErrorMessage() {
        if (seasonInput.isEmpty()) {
            if (episodeInput.isEmpty()) {
                return "Missing data: Seasons - Episodes";
            }
            return "Missing data: Seasons";
        }
        if (episodeInput.isEmpty()) {
            return "Missing data: Episodes";
        }
        return "";
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_pager_add_entry_mode_1;
    }
    //endregion
}
