package com.ombrax.watchers.Views.Card;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Manager.SettingsManager;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Repositories.WatchRepository;
import com.ombrax.watchers.Utils.DialogUtils;
import com.ombrax.watchers.Views.Button.SquareOptionButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ombrax on 22/08/2015.
 */
public class WatchCardOptionsView extends FrameLayout {

    //region variable
    private WatchModel watchModel;
    private Dialog parentDialog;
    //endregion

    //region inner field
    private DomainController dc;
    private WatchRepository repo;
    private SettingsManager settingsManager;
    //endregion

    //region view
    private SquareOptionButton nextButton;
    private SquareOptionButton undoButton;
    private SquareOptionButton editButton;
    private SquareOptionButton archiveButton;
    private SquareOptionButton deleteButton;
    private SquareOptionButton closeButton;

    private Drawable avatarDrawable;
    //endregion

    //region constructor
    public WatchCardOptionsView(Context context, WatchModel watchModel, Dialog parentDialog) {
        super(context);
        this.watchModel = watchModel;
        this.parentDialog = parentDialog;
        init();
    }
    //endregion

    //region setup
    private void init() {
        dc = DomainController.getInstance();
        repo = WatchRepository.getInstance();
        settingsManager = SettingsManager.getInstance();

        inflate(getContext(), R.layout.view_card_options, this);
        getViews();
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setListeners();
        getAvatar();
    }
    //endregion

    //region helper
    private void getViews() {
        nextButton = (SquareOptionButton) findViewById(R.id.view_card_options_next_button);
        undoButton = (SquareOptionButton) findViewById(R.id.view_card_options_undo_button);
        editButton = (SquareOptionButton) findViewById(R.id.view_card_options_edit_button);
        archiveButton = (SquareOptionButton) findViewById(R.id.view_card_options_archive_button);
        deleteButton = (SquareOptionButton) findViewById(R.id.view_card_options_delete_button);
        closeButton = (SquareOptionButton) findViewById(R.id.view_card_options_close_button);
    }

    private void setListeners() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchModel.increment()) {
                    update();
                } else {
                    if (settingsManager.isConfirmOnCompleteEnabled()) {
                        DialogUtils.newAvatarAlertDialog(
                                getContext(),
                                watchModel.getName(),
                                "Conclude Tv Show",
                                avatarDrawable,
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        performOnCompleteAction();
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                }, null
                        ).show();
                    } else {
                        performOnCompleteAction();
                    }
                    parentDialog.dismiss();
                }
            }
        });
        undoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (watchModel.decrement()) {
                    update();
                }
            }
        });
        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setEnabled(false);
                dc.notifyListItemObservers(watchModel);
                parentDialog.dismiss();
            }
        });

        archiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                archiveButton.setEnabled(false);
                archive();
                parentDialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButton.setEnabled(false);
                DialogUtils.newAvatarAlertDialog(
                        getContext(),
                        watchModel.getName(),
                        "Delete from list",
                        avatarDrawable,
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                delete();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        },
                        null
                ).show();
                parentDialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeButton.setEnabled(false);
                parentDialog.dismiss();
            }
        });
    }

    private void performOnCompleteAction() {
        watchModel.setCompleted(true);
        switch (settingsManager.getOnCompleteAction()) {
            case NONE:
                update();
                break;
            case ARCHIVE:
                archive();
                break;
            case DELETE:
                delete();
                break;
        }
    }

    private void getAvatar() {
        avatarDrawable = Drawable.createFromPath(watchModel.getThumbnailPath());
        if (avatarDrawable == null) {
            avatarDrawable = getResources().getDrawable(R.drawable.img_not_available);
        }
    }
    //endregion

    //region persistence
    private void update() {
        repo.update(watchModel, true);
        dc.handleWatchCardUpdate();
    }

    private void archive() {
        watchModel.setArchived(true);
        repo.update(watchModel, false);
        dc.handleWatchCardRemove(watchModel);
    }

    private void delete() {
        repo.delete(watchModel.getId());
        dc.handleWatchCardRemove(watchModel);
    }
    //endregion

}
