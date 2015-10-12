package com.ombrax.watchers.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Ombrax on 8/08/2015.
 */
public class LayoutUtils {

    public static LinearLayout.LayoutParams newLinearLayoutParams(int width, int height, int weight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.weight = weight;
        return layoutParams;
    }

    public static void clearFocusOnKeyboardHide(final View dummy){
        dummy.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dummy.requestFocus();
            }
        }, 150);
    }
}
