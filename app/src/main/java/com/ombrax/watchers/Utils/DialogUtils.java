package com.ombrax.watchers.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

import com.ombrax.watchers.Controllers.DomainController;
import com.ombrax.watchers.Interfaces.Handler.IWatchCardUpdateHandler;
import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Card.WatchCardOptionsView;
import com.rey.material.app.BottomSheetDialog;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ombrax on 24/08/2015.
 */
public class DialogUtils {

    //region constant
    private static final int avatarSize = 160;
    private static final int animDuration = 300;
    //endregion

    //region method
    public static SweetAlertDialog newAvatarAlertDialog(Context context, String title, String message, Drawable avatar, SweetAlertDialog.OnSweetClickListener onConfirmListener) {
        return newAvatarAlertDialog(context, title, message, avatar, onConfirmListener, null);
    }

    public static SweetAlertDialog newAvatarAlertDialog(Context context, String title, String message, Drawable avatar, SweetAlertDialog.OnSweetClickListener onConfirmListener, DialogInterface.OnDismissListener onDismissListener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.Type.CUSTOM)
                .setDialogBackground(R.drawable.dialog_light_background)
                .setImageSize(avatarSize)
                .setImage(ImageUtils.getScaledCircularBitmap(avatar, avatarSize))
                .setTitleText(title)
                .setContentText(message)
                .displayCancelButton(true)
                .setCancelText("Cancel")
                .setCancelSelector(R.drawable.button_cancel_background)
                .setConfirmText("OK")
                .setConfirmSelector(R.drawable.button_confirm_background)
                .setOnConfirmClickListener(onConfirmListener);
        if (onDismissListener != null) {
            dialog.setOnDismissListener(onDismissListener);
        }
        return dialog;
    }

    public static SweetAlertDialog newAvatarChoiceDialog(Context context, String title, String message, Drawable avatar, SweetAlertDialog.OnSweetClickListener onResumeListener, SweetAlertDialog.OnSweetClickListener onDeleteListener, DialogInterface.OnDismissListener onDismissListener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.Type.CUSTOM)
                .setDialogBackground(R.drawable.dialog_light_background)
                .setImageSize(avatarSize)
                .setImage(ImageUtils.getScaledCircularBitmap(avatar, avatarSize))
                .setTitleText(title)
                .setContentText(message)
                .displayCancelButton(true)
                .setCancelOnTouchOutside(true)
                .setCancelText("Delete")
                .setCancelSelector(R.drawable.button_delete_background)
                .setConfirmText("Resume")
                .setConfirmSelector(R.drawable.button_confirm_background)
                .setOnConfirmClickListener(onResumeListener)
                .setOnCancelClickListener(onDeleteListener);
        if (onDismissListener != null) {
            dialog.setCancelable(true);
            dialog.setOnDismissListener(onDismissListener);
        }
        return dialog;
    }

    public static BottomSheetDialog newWatchCardOptionsDialog(Context context, IWatchCardUpdateHandler watchCardUpdateHandler, WatchModel watchModel) {
        DomainController.getInstance().setWatchCardUpdateHandler(watchCardUpdateHandler);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.contentView(new WatchCardOptionsView(context, watchModel, dialog))
                .heightParam(FrameLayout.LayoutParams.WRAP_CONTENT)
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .inDuration(animDuration)
                .outDuration(animDuration);
        return dialog;
    }

    public static SweetAlertDialog newMissingInputDialog(Context context, String missingElements) {
        return new SweetAlertDialog(context, SweetAlertDialog.Type.ERROR)
                .setDialogBackground(R.drawable.dialog_light_background)
                .setTitleText("Missing Data")
                .setContentText(missingElements)
                .setContentTextStyle(Typeface.BOLD)
                .setContentTextColor(Color.parseColor("#DD6B55"))
                .setConfirmText("OK")
                .setConfirmSelector(R.drawable.button_confirm_background);
    }
    //endregion
}
