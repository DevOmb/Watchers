package com.ombrax.watchers.Utils;

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

    public static LinearLayout.MarginLayoutParams newLinearMarginLayoutParams(int width, int height, int margin) {
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(width, height);
        layoutParams.setMargins(margin, margin, margin, margin);
        return layoutParams;
    }

    public static ViewGroup.MarginLayoutParams newMarginLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(width, height);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        return layoutParams;
    }
}
