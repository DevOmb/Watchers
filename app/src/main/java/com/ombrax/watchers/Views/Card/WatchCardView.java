package com.ombrax.watchers.Views.Card;

import android.content.Context;
import android.content.DialogInterface;
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
    private DomainController dc;
    private WatchRepository repo;
    private SettingsManager settingsManager;

    private ViewHolder vh;

    private boolean isArchiveDialogShowing;
    private boolean isDeleteDialogShowing;
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
            setListeners();
            setTag(vh);
        }

        displayModelAttributes();
    }
    //endregion

    //region helper
    //region setup
    private void loadResources() {
        dc = DomainController.getInstance();
        repo = WatchRepository.getInstance();
        settingsManager = SettingsManager.getInstance();

        r = getResources();
        textColorAccent = r.getColor(R.color.accent);
        textColorDefault = r.getColor(R.color.holo_white);
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

        if (settingsManager.isDisplayBannerEnabled()) {
            createBanner();
        }
    }

    private void setListeners() {
        if (watchModel.isArchived()) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (watchModel.isArchived() && !isArchiveDialogShowing) {
                        isArchiveDialogShowing = true;
                        DialogUtils.newAvatarAlertDialog(
                                getContext(),
                                watchModel.getName(),
                                watchModel.isCompleted() ? "Delete from archive" : "Withdraw from archive",
                                vh.thumbnailImage.getDrawable(),
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (watchModel.isCompleted()) {
                                            repo.delete(watchModel.getId());
                                            dc.handleWatchCardRemove(watchModel);
                                        } else {
                                            watchModel.setArchived(false);
                                            repo.update(watchModel, false);
                                            dc.handleWatchCardRemove(watchModel);
                                        }
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                },
                                new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        isArchiveDialogShowing = false;
                                    }
                                }
                        ).show();
                    }
                }
            });
        }
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!watchModel.isArchived()) {
                    DialogUtils.newWatchCardOptionsDialog(getContext(), WatchCardView.this, watchModel).show();
                }
                return true;
            }
        });
    }
    //endregion


    //region create
    private void createBanner() {
        vh.banner = new Banner(getContext());
        vh.banner.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        vh.banner.setBackgroundColor(textColorAccent);
        vh.banner.setText("New");
        vh.banner.attachTo(vh.thumbnailImage);
    }
    //endregion

    //region display
    private void displayModelAttributes() {
        vh.titleField.setText(watchModel.getName());
        vh.progressLabel.setVisibility(settingsManager.isDisplayProgressEnabled() ? VISIBLE : GONE);
        loadImageFromPath(watchModel.getThumbnailPath());
        updateDisplay();
    }

    private void loadImageFromPath(String path) {
        ImageUtils.loadImageFromFile(vh.thumbnailImage, path);
    }

    private void updateDisplay() {
        vh.progressBar.setProgress(watchModel.getProgress());
        vh.progressLabel.setText(String.format("%.2f%%", watchModel.getProgress() * 100));
        vh.seasonEpisodeField.setText(watchModel.format(WatchModel.Format.WATCH));

        if (!watchModel.isCompleted()) {
            boolean watching = watchModel.nowWatching();
            vh.dateField.setText(watching ? "Last seen" : "New");
            vh.dateField.setTypeface(watching ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
            vh.dateField.setTextColor(watching ? textColorDefault : textColorAccent);
            vh.lastSeenField.setText(watching ? watchModel.format(WatchModel.Format.DATE) : "");

            boolean defaultState = watchModel.getWatchState() == WatchState.DEFAULT || watchModel.getWatchState() == WatchState.FIRST_EPISODE;
            vh.nextField.setTextColor(defaultState ? textColorDefault : textColorAccent);
            vh.nextField.setTypeface(defaultState ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
            vh.nextField.setText(watchModel.getWatchState().toString());

            if (vh.banner != null) {
                vh.banner.setVisibility(watching ? GONE : VISIBLE);
            }
        } else {
            setOnLongClickListener(null);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isDeleteDialogShowing) {
                        isDeleteDialogShowing = true;
                        DialogUtils.newAvatarAlertDialog(
                                getContext(),
                                watchModel.getName(),
                                "Delete from list",
                                vh.thumbnailImage.getDrawable(),
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        repo.delete(watchModel.getId());
                                        dc.handleWatchCardRemove(watchModel);
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                }, new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        isDeleteDialogShowing = false;
                                    }
                                }
                        ).show();
                    }
                }
            });

            setAlpha(0.4f);

            vh.nextField.setTextColor(textColorDefault);
            vh.nextField.setTypeface(Typeface.DEFAULT);
            vh.nextField.setText("Done");

            vh.dateField.setText("Completed on");
            vh.dateField.setTypeface(Typeface.DEFAULT);
            vh.dateField.setTextColor(textColorDefault);
            vh.lastSeenField.setText(watchModel.format(WatchModel.Format.DATE));
        }
    }
    //endregion
    //endregion


    //region interface implementation
    @Override
    public void handleWatchCardUpdate() {
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
