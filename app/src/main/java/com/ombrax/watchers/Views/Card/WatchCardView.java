package com.ombrax.watchers.Views.Card;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ombrax.banner.View.Banner;
import com.ombrax.simpleprogressbar.View.SimpleProgressBar;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Enums.WatchState;
import com.ombrax.watchers.Interfaces.Handler.IWatchCardUpdateHandler;
import com.ombrax.watchers.Manager.SettingsManager;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.DialogUtils;
import com.ombrax.watchers.Utils.ImageUtils;
import com.rey.material.app.BottomSheetDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ombrax on 15/07/2015.
 */
public class WatchCardView extends FrameLayout implements IWatchCardUpdateHandler {

    //region declaration

    //region variable
    private WatchModel watchModel;
    //endregion

    //region inner field
    private WatchRepository repo;
    private SettingsManager manager;
    private ViewHolder vh;
    //endregion

    //region resource
    private Resources r;

    private int textColorAccent;
    private int textColorDefault;
    //endregion

    //endregion

    //region constructor
    public WatchCardView(Context context) {
        super(context);
        inflate(context, R.layout.view_card, this);
        loadResources();
    }
    //endregion

    //region model
    public void setModel(WatchModel watchModel) {
        this.watchModel = watchModel;

        vh = (ViewHolder) getTag();
        if (vh == null) {
            vh = new ViewHolder();
            getViews();
            addClickListeners();
            setTag(vh);
        }

        applyModel();
    }
    //endregion

    //region helper
    private void loadResources() {
        repo = WatchRepository.getInstance();
        manager = SettingsManager.getInstance();
        r = getResources();


        textColorAccent = r.getColor(R.color.accent);
        textColorDefault = r.getColor(R.color.dark_grey);
    }

    private void getViews() {
        vh.thumbnailImage = (ImageView) findViewById(R.id.watch_card_thumbnail);
        vh.titleField = (TextView) findViewById(R.id.watch_card_title);

        vh.progressLabel = (TextView) findViewById(R.id.watch_card_progress);
        vh.progressBar = (SimpleProgressBar) findViewById(R.id.watch_card_progress_bar);

        vh.nextField = (TextView) findViewById(R.id.watch_card_next_label);
        vh.seasonEpisodeField = (TextView) findViewById(R.id.watch_card_seasonEpisode);
        vh.dateField = (TextView) findViewById(R.id.watch_card_date_label);
        vh.lastSeenField = (TextView) findViewById(R.id.watch_card_lastViewed);

        if(manager.isDisplayBannerEnabled()) {
            createBanner();
        }
    }

    private void createBanner() {
        vh.banner = new Banner(getContext());
        vh.banner.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        vh.banner.setBackgroundColor(textColorAccent);
        vh.banner.setText("New");
        vh.banner.attachTo(vh.thumbnailImage);
    }

    private void addClickListeners() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchModel.isArchived()) {
                    DialogUtils.newAvatarAlertDialog(getContext(), watchModel.getName(), "Withdraw from archive", vh.thumbnailImage.getDrawable(), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            watchModel.setArchived(false);
                            repo.update(watchModel, false);
                            DomainController.getInstance().handleWatchCardRemove(watchModel);
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();
                } else {
                    if (watchModel.increment()) {
                        repo.update(watchModel, true);
                        updateDisplay();
                    } else {
                        if(manager.isConfirmOnCompleteEnabled()) {
                            DialogUtils.newAvatarAlertDialog(getContext(), watchModel.getName(), "Conclude Tv Show", vh.thumbnailImage.getDrawable(), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    //TODO Check action on complete in settings
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            }).show();
                        }
                    }
                }
            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!watchModel.isArchived()) {
                    DomainController.getInstance().setWatchCardUpdateHandler(WatchCardView.this);
                    DialogUtils.newWatchCardOptionsDialog(getContext(), watchModel).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void applyModel() {
        vh.titleField.setText(watchModel.getName());
        vh.progressLabel.setVisibility(manager.isDisplayProgressEnabled() ? VISIBLE : GONE);
        loadImageFromPath(watchModel.getThumbnailPath());
        updateDisplay();
    }

    private void updateDisplay() {
        //Progress
        vh.progressBar.setProgress(watchModel.getProgress());
        vh.progressLabel.setText(String.format("%.2f%%", watchModel.getProgress() * 100));
        vh.seasonEpisodeField.setText(watchModel.format(WatchModel.Format.WATCH));

        //New
        boolean watching = watchModel.nowWatching();
        vh.dateField.setText(watching ? "Last seen" : "New");
        vh.dateField.setTypeface(watching ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
        vh.dateField.setTextColor(watching ? textColorDefault : textColorAccent);
        vh.lastSeenField.setText(watching ? watchModel.format(WatchModel.Format.DATE) : "");

        //State
        boolean defaultState = watchModel.getWatchState() == WatchState.DEFAULT || watchModel.getWatchState() == WatchState.FIRST_EPISODE;
        vh.nextField.setTextColor(defaultState ? textColorDefault : textColorAccent);
        vh.nextField.setTypeface(defaultState ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
        vh.nextField.setText(watchModel.getWatchState().toString());

        //Banner
        if (vh.banner != null) {
            vh.banner.setVisibility(watching ? GONE : VISIBLE);
        }
    }

    private void loadImageFromPath(String path) {
        ImageUtils.loadImageFromFile(vh.thumbnailImage, path);
    }
    //endregion

    //region interface implementation
    @Override
    public void handlerWatchCardUpdate() {
        updateDisplay();
    }
    //endregion

    //region viewholder
    private class ViewHolder {

        private ImageView thumbnailImage;
        private TextView titleField;

        private TextView progressLabel;
        private SimpleProgressBar progressBar;

        private TextView nextField;
        private TextView seasonEpisodeField;
        private TextView dateField;
        private TextView lastSeenField;

        private Banner banner;
    }
    //endregion

}
