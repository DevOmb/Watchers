package com.ombrax.watchers.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Views.Card.WatchCardOptionsView;
import com.rey.material.app.BottomSheetDialog;

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
        return new SweetAlertDialog(context, SweetAlertDialog.Type.CUSTOM)
                .setDialogBackground(R.drawable.light_dialog_background)
                .setImageSize(avatarSize)
                .setImage(ImageUtils.getScaledCircularBitmap(avatar, avatarSize))
                .setTitleText(title)
                .setContentText(message)
                .displayCancelButton(true)
                .setCancelText("Cancel")
                .setCancelSelector(R.drawable.cancel_button_background)
                .setConfirmText("OK")
                .setConfirmSelector(R.drawable.confirm_button_background)
                .setOnConfirmClickListener(onConfirmListener);
    }

    public static BottomSheetDialog newWatchCardOptionsDialog(Context context, WatchModel watchModel){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.contentView(new WatchCardOptionsView(context, watchModel, dialog))
                .heightParam(FrameLayout.LayoutParams.WRAP_CONTENT)
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .inDuration(animDuration)
                .outDuration(animDuration);
        return dialog;
    }
    //endregion
}
