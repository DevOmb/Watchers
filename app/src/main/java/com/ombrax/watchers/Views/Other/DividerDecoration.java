package com.ombrax.watchers.Views.Other;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ombrax on 7/08/2015.
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    public DividerDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = spacing;
    }
}
