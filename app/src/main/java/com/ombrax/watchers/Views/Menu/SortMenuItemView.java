package com.ombrax.watchers.Views.Menu;

import android.content.Context;
import android.view.View;

import com.ombrax.animatedsortordericon.AnimatedSortingOrderIcon;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 25/07/2015.
 */
public class SortMenuItemView extends MenuItemView {

    //region inner field
    private int defaultPadding;
    private AnimatedSortingOrderIcon orderIcon;
    //endregion

    //region constructor
    public SortMenuItemView(Context context) {
        super(context);
        init();
    }
    //endregion

    //region setup
    private void init() {
        getDefaultResources();
        transferAttributes();
        addAnimatedIcon();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderIcon.getVisibility() != GONE) {
                    orderIcon.transform();
                }
                mc.onMenuItemSelect((MenuItemType) getTag());
            }
        });
    }
    //endregion

    //region method
    public boolean isAscendingOrder() {
        return orderIcon.isAscendingOrder();
    }
    //endregion

    //region helper
    private void getDefaultResources() {
        defaultPadding = getResources().getDimensionPixelSize(R.dimen.default_2x);
    }

    private void transferAttributes() {
        setBackgroundResource(R.drawable.default_menu_selector);
        title.setBackgroundResource(R.color.transparent);
        setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
        title.setPadding(0, 0, 0, 0);
    }

    private void addAnimatedIcon() {
        orderIcon = new AnimatedSortingOrderIcon(getContext());
        orderIcon.setDimensions(32, 32);
        orderIcon.setBars(4);
        orderIcon.setBarSpacing(0.66f);
        orderIcon.setBarIncrement(0.20f);
        orderIcon.setBarCornerRadius(1.0f);
        orderIcon.setBarColor(getResources().getColor(R.color.holo_white));
        orderIcon.setAnimationLength(400);
        orderIcon.setVisibility(GONE);
        addView(orderIcon);
    }
    //endregion

    //region override
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if(selected){
            orderIcon.setVisibility(VISIBLE);
            orderIcon.setAscendingOrder(true);
        }else{
            orderIcon.setVisibility(GONE);
        }
    }
    //endregion
}
