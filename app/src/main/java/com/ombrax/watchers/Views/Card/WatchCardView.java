package com.ombrax.watchers.Views.Card;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ombrax.banner.View.Banner;
import com.ombrax.simpleprogressbar.View.SimpleProgressBar;
import com.ombrax.watchers.Animations.HeightAnimation;
import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Enums.WatchState;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.ImageUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ombrax on 15/07/2015.
 */
public class WatchCardView extends FrameLayout {

    //region declaration

    //region constant
    private static final int DEFAULT_IMAGE_RESOURCE = R.drawable.thumb;
    //endregion

    //region variable
    private ICardManager manager;
    private WatchModel watchModel;
    //endregion

    //region inner field
    private ViewHolder vh;
    private WatchRepository repo;
    private HeightAnimation animation;
    private boolean thumbnailLoaded;
    //endregion

    //region resource
    private Resources r;

    private int ACCENT;
    private int DEFAULT;
    //endregion

    //endregion

    //region constructor
    public WatchCardView(Context context, ICardManager manager) {
        super(context);
        this.manager = manager;
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
            addExpansionAnimation();
            setTag(vh);
        }

        applyModel();
    }
    //endregion

    //region helper
    private void loadResources() {
        repo = WatchRepository.getInstance();
        r = getResources();

        ACCENT = r.getColor(R.color.accent);
        DEFAULT = r.getColor(R.color.dark_grey);
    }

    private void getViews() {
        vh.content = (LinearLayout) findViewById(R.id.watch_card_content);
        vh.thumbnailImage = (ImageView) findViewById(R.id.watch_card_thumbnail);
        vh.titleField = (TextView) findViewById(R.id.watch_card_title);

        vh.progressLabel = (TextView) findViewById(R.id.watch_card_progress);
        vh.progressBar = (SimpleProgressBar) findViewById(R.id.watch_card_progress_bar);

        vh.nextField = (TextView) findViewById(R.id.watch_card_next_label);
        vh.seasonEpisodeField = (TextView) findViewById(R.id.watch_card_seasonEpisode);
        vh.dateField = (TextView) findViewById(R.id.watch_card_date_label);
        vh.lastSeenField = (TextView) findViewById(R.id.watch_card_lastViewed);

        vh.settingsLayout = (LinearLayout) findViewById(R.id.watch_card_expandable);
        vh.decrementButton = (ImageButton) findViewById(R.id.watch_card_minus);
        vh.editButton = (ImageButton) findViewById(R.id.watch_card_edit);
        vh.archiveButton = (ImageButton) findViewById(R.id.watch_card_archive);
        vh.removeButton = (ImageButton) findViewById(R.id.watch_card_delete);
        vh.cancelButton = (ImageButton) findViewById(R.id.watch_card_cancel);

        createBanner();
    }

    private void createBanner() {
        vh.banner = new Banner(getContext());
        vh.banner.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        vh.banner.setBackgroundColor(ACCENT);
        vh.banner.setText("New");
        vh.banner.attachTo(vh.content);
    }

    private void addExpansionAnimation() {
        animation = new HeightAnimation(vh.settingsLayout, r.getDimensionPixelSize(R.dimen.expandable_height), 700);
    }

    private void addClickListeners() {
        vh.content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click: " + vh.thumbnailImage.getWidth() + " - " + vh.thumbnailImage.getHeight());
                if (watchModel.increment()) {
                    repo.update(watchModel, true);
                    updateDisplay();
                } else {
                    //TODO check confirm complete enabled in settings
                    confirmCompletion(watchModel.getName());
                }
            }
        });

        vh.content.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (vh.settingsLayout.getVisibility() != View.VISIBLE) {
                    vh.settingsLayout.startAnimation(animation);
                    invalidate();
                }
                return true;
            }
        });

        vh.decrementButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchModel.decrement()) {
                    repo.update(watchModel, true);
                    updateDisplay();
                }
            }
        });

        vh.editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DomainController.getInstance().notifyListItemObservers(watchModel);
            }
        });

        vh.archiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                watchModel.setArchived(true);
                //repo.update(watchModel, false);
                manager.remove(watchModel);
            }
        });

        vh.removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //repo.delete(watchModel.getId());
                manager.remove(watchModel);
            }
        });

        vh.cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.settingsLayout.startAnimation(animation);
            }
        });
    }

    private void applyModel() {
        vh.titleField.setText(watchModel.getName());
        loadImageFromPath(watchModel.getThumbnailPath());
        updateDisplay();
    }

    private void updateDisplay() {
        //PROGRESS
        vh.progressBar.setProgress(watchModel.getProgress());
        vh.progressLabel.setText(String.format("%.2f%%", watchModel.getProgress() * 100));
        vh.seasonEpisodeField.setText(watchModel.format(WatchModel.Format.WATCH));

        //New
        boolean watching = watchModel.nowWatching();
        vh.dateField.setText(watching ? "Last seen" : "New");
        vh.dateField.setTypeface(watching ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
        vh.dateField.setTextColor(watching ? DEFAULT : ACCENT);
        vh.lastSeenField.setText(watching ? watchModel.format(WatchModel.Format.DATE) : "");

        //State
        boolean defaultState = watchModel.getWatchState() == WatchState.DEFAULT || watchModel.getWatchState() == WatchState.FIRST_EPISODE;
        vh.nextField.setTextColor(defaultState ? DEFAULT : ACCENT);
        vh.nextField.setTypeface(defaultState ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
        vh.nextField.setText(watchModel.getWatchState().toString());

        //Banner
        vh.banner.setVisibility(watching ? GONE : VISIBLE);
    }

    private void loadImageFromPath(String path) {
        //TODO add flash /!\ icon in (left upper) corner if image not found (deleted or invalid path)
        if (!thumbnailLoaded) {
            if (path != null && !path.isEmpty()) {
                Picasso.with(getContext())
                        .load(new File(path))
                        .placeholder(R.drawable.loading_placeholder)
                        .error(DEFAULT_IMAGE_RESOURCE)
                        .into(vh.thumbnailImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                //TODO check display error icon enabled in settings
                            }
                        });
            } else {
                Picasso.with(getContext())
                        .load(ImageUtils.getSingleResource())//TEMP replace with default resource
                        .placeholder(R.drawable.loading_placeholder)
                        .into(vh.thumbnailImage);
            }
            thumbnailLoaded = true;
        }
    }

    private void confirmCompletion(String tvShow) {
        int avatarSize = 160;
        //TODO make default (extend) implementation
        new SweetAlertDialog(getContext(), SweetAlertDialog.Type.CUSTOM)
                .setDialogBackground(R.drawable.dialog_background)
                .setImageSize(avatarSize)
                .setImage(ImageUtils.getScaledCircularBitmap(vh.thumbnailImage.getDrawable(), avatarSize))
                .setTitleText(tvShow)
                .setContentText("Conclude TV show")
                .displayCancelButton(true)
                .setCancelText("Cancel")
                .setCancelSelector(R.drawable.cancel_button_background)
                .setConfirmText("OK")
                .setConfirmSelector(R.drawable.confirm_button_background)
                .setOnConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
    //endregion

    //region viewholder
    private class ViewHolder {

        private LinearLayout content;
        private ImageView thumbnailImage;
        private TextView titleField;

        private TextView progressLabel;
        private SimpleProgressBar progressBar;

        private TextView nextField;
        private TextView seasonEpisodeField;
        private TextView dateField;
        private TextView lastSeenField;

        private LinearLayout settingsLayout;
        private ImageButton decrementButton;
        private ImageButton editButton;
        private ImageButton archiveButton;
        private ImageButton removeButton;
        private ImageButton cancelButton;

        private Banner banner;
    }
    //endregion

    //region interface
    public interface ICardManager {
        void remove(WatchModel watchModel);
    }
    //endregion
}
