package cn.pedant.SweetAlert;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SweetAlertDialog extends Dialog implements View.OnClickListener {

    //region view
    private View mDialogView;
    private LinearLayout mDialogLayout;

    private ImageView mImage;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private Button mConfirmButton;
    private Button mCancelButton;

    private FrameLayout mErrorFrame;
    private ImageView mErrorX;

    private FrameLayout mSuccessFrame;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;
    private SuccessTickView mSuccessTick;

    private FrameLayout mWarningFrame;
    //endregion

    //region animation
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;
    //endregion

    //region variable
    private Type mAlertType;
    private int mDialogBackground;

    private int mImageSize;
    private Drawable mImgDrawable;

    private int mTitleTextColor;
    private int mTitleTextSize;
    private String mTitleText;

    private int mContentTextColor;
    private int mContentTextSize;
    private String mContentText;

    private String mCancelText;
    private boolean mShowCancel;
    private int mCancelSelector;

    private String mConfirmText;
    private boolean mShowContent;
    private int mConfirmSelector;

    private boolean mCloseFromCancel;
    //endregion

    //region listener
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    //endregion

    //region constructor
    public SweetAlertDialog(Context context) {
        this(context, Type.DEFAULT);
    }

    public SweetAlertDialog(Context context, Type alertType) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mAlertType = alertType;
        mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_rotate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            SweetAlertDialog.super.cancel();
                        } else {
                            SweetAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);
    }
    //endregion

    //region create
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        //Get views
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mDialogLayout = (LinearLayout) findViewById(R.id.dialog);
        mImage = (ImageView) findViewById(R.id.image);
        mTitleTextView = (TextView) findViewById(R.id.title_text);
        mContentTextView = (TextView) findViewById(R.id.content_text);
        mConfirmButton = (Button) findViewById(R.id.confirm_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
        mErrorX = (ImageView) mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = (FrameLayout) findViewById(R.id.success_frame);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mSuccessTick = (SuccessTickView) mSuccessFrame.findViewById(R.id.success_tick);
        mWarningFrame = (FrameLayout) findViewById(R.id.warning_frame);

        //Set listeners
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        //Set initial values
        init();

        //Set remaining values according to type
        setAlertType(mAlertType, true);
    }
    //endregion

    //region helper
    private void init() {
        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);

        if (mDialogBackground != 0) {
            setDialogBackground(mDialogBackground);
        }
        if (mImageSize != 0) {
            setImageSize(mImageSize);
        }
        if (mTitleTextColor != 0) {
            setTitleTextColor(mTitleTextColor);
        }
        if (mTitleTextSize != 0) {
            setTitleTextSize(mTitleTextSize);
        }
        if (mContentTextColor != 0) {
            setContentTextColor(mContentTextColor);
        }
        if (mContentTextSize != 0) {
            setContentTextSize(mContentTextSize);
        }
        if(mCancelSelector != 0){
            setCancelSelector(mCancelSelector);
        }
        if(mConfirmSelector != 0){
            setConfirmSelector(mConfirmSelector);
        }
    }

    private void restoreDefaultState() {
        mImage.setVisibility(View.GONE);
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);
        mWarningFrame.setVisibility(View.GONE);
        mConfirmButton.setVisibility(View.VISIBLE);

        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    private void playAnimation() {
        if (mAlertType == Type.ERROR) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == Type.SUCCESS) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void setAlertType(Type alertType, boolean fromCreate) {
        mAlertType = alertType;
        if (mDialogView != null) {
            if (!fromCreate) {
                restoreDefaultState();
            }
            switch (mAlertType) {
                case ERROR:
                    mErrorFrame.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    mSuccessFrame.setVisibility(View.VISIBLE);
                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                    break;
                case WARNING:
                    mConfirmButton.setBackgroundResource(mConfirmSelector != 0 ? mConfirmSelector : R.drawable.red_button_background);
                    mWarningFrame.setVisibility(View.VISIBLE);
                    break;
                case CUSTOM:
                    setImage(mImgDrawable);
                    break;
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }
    //endregion

    //region getter
    public Type getAlertType() {
        return mAlertType;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public String getContentText() {
        return mContentText;
    }

    public boolean isCancelButtonDisplayed() {
        return mShowCancel;
    }

    public boolean isContentTextDisplayed() {
        return mShowContent;
    }

    public String getCancelText() {
        return mCancelText;
    }

    public String getConfirmText() {
        return mConfirmText;
    }
    //endregion

    //region setter
    public SweetAlertDialog setCancelOnTouchOutside(boolean cancel) {
        setCancelable(true);
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    public SweetAlertDialog setDialogBackground(int resourceId) {
        mDialogBackground = resourceId;
        if (mDialogLayout != null) {
            mDialogLayout.setBackgroundResource(resourceId);
        }
        return this;
    }

    public SweetAlertDialog setAlertType(Type alertType) {
        setAlertType(alertType, false);
        return this;
    }

    public SweetAlertDialog setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public SweetAlertDialog setTitleTextColor(int textColor) {
        mTitleTextColor = textColor;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(textColor);
        }
        return this;
    }

    public SweetAlertDialog setTitleTextSize(int spValue) {
        mTitleTextSize = spValue;
        if (mTitleTextView != null) {
            mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spValue);
        }
        return this;
    }

    public SweetAlertDialog setContentText(String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            displayContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public SweetAlertDialog setContentTextColor(int textColor) {
        mContentTextColor = textColor;
        if (mContentTextView != null) {
            mContentTextView.setTextColor(textColor);
        }
        return this;
    }

    public SweetAlertDialog setContentTextSize(int spValue) {
        mContentTextSize = spValue;
        if (mContentTextView != null) {
            mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spValue);
        }
        return this;
    }

    public SweetAlertDialog setImageSize(int dimension) {
        mImageSize = dimension;
        if (mImage != null) {
            mImage.setLayoutParams(new LinearLayout.LayoutParams(dimension, dimension));
        }
        return this;
    }

    public SweetAlertDialog setImage(Drawable drawable) {
        mImgDrawable = drawable;
        if (mImage != null && mImgDrawable != null) {
            mImage.setVisibility(View.VISIBLE);
            mImage.setImageDrawable(mImgDrawable);
        }
        return this;
    }

    public SweetAlertDialog setImage(int resourceId) {
        return setImage(getContext().getResources().getDrawable(resourceId));
    }

    public SweetAlertDialog setImage(Bitmap bitmap) {
        return setImage(new BitmapDrawable(getContext().getResources(), bitmap));
    }

    public SweetAlertDialog displayCancelButton(boolean display) {
        mShowCancel = display;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public SweetAlertDialog setCancelSelector(int resourceId) {
        mCancelSelector = resourceId;
        if (mCancelButton != null) {
            mCancelButton.setBackgroundResource(resourceId);
        }
        return this;
    }

    public SweetAlertDialog setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            displayCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public SweetAlertDialog displayContentText(boolean display) {
        mShowContent = display;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public SweetAlertDialog setConfirmSelector(int resourceId) {
        mConfirmSelector = resourceId;
        if (mConfirmButton != null) {
            mConfirmButton.setBackgroundResource(resourceId);
        }
        return this;
    }

    public SweetAlertDialog setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public SweetAlertDialog setOnCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public SweetAlertDialog setOnConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }
    //endregion

    //region method

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }
    //endregion

    //region override

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }
    //endregion

    //region interface implementation
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(SweetAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }
    //endregion

    //region interface
    public interface OnSweetClickListener {
        void onClick(SweetAlertDialog sweetAlertDialog);
    }
    //endregion

    //region enum
    public enum Type {
        DEFAULT,
        ERROR,
        SUCCESS,
        WARNING,
        CUSTOM
    }
    //endregion
}