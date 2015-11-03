package com.ombrax.watchers.Fragments.PagerFragment;

import android.view.View;
import android.widget.EditText;

import com.devomb.cellgriddialog.CellGridDialogBuilder;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Other.NumericInputField;

/**
 * Created by Ombrax on 9/10/2015.
 */
public class WatchAddEntryMode3PagerFragment extends WatchAddEntryModePagerFragment {

    //region view
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

        //Episode
        registerKeyboardListener(episodeInput);
        episodeInput.setOnFocusChangeListener(new NumericInputField.OnFocusChangeListener() {
            @Override
            public void onFocusLost(EditText source, boolean hasValidInput, int value) {
                setStartAtInputEnabled(hasValidInput);
                if (hasValidInput) {
                    model.setSeasonEpisodeCount(value);
                    if (model.getCurrentEpisode() > value) {
                        model.setCurrentEpisode(value);
                        updateStartAt();
                    }
                }
            }
        });

        //Start at
        startAtLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CellGridDialogBuilder.create(getChildFragmentManager(), model.getSeasonEpisodeCount())
                        .header("Season ", "Episodes")
                        .select(model.getCurrentEpisode() - 1)
                        .onAcceptListener(WatchAddEntryMode3PagerFragment.this)
                        .show();
            }
        });
    }
    //endregion

    //region helper
    private void getViews(View view) {
        episodeInput = (NumericInputField) view.findViewById(R.id.fragment_add_episode_input);
    }

    private void setModel() {
        setStartAtInputEnabled(model != null);
        if (model == null) {
            model = new WatchModel();
            model.setEpisodesOnly(true);
        } else {
            episodeInput.setText(String.valueOf(model.getSeasonEpisodeCount()));
        }
    }
    //endregion

    //region override
    @Override
    public String getStartAtErrorMessage() {
        if (episodeInput.isEmpty()) {
            return "Missing data: Episodes";
        }
        return "";
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_pager_add_entry_mode_3;
    }
    //endregion

}
