package com.ombrax.watchers.Views.Menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Views.Button.LabeledIconButton;

/**
 * Created by Ombrax on 6/08/2015.
 */
public class SortMenuView extends MenuView{

    //region inner field
    private LabeledIconButton acceptButton;
    private LabeledIconButton declineButton;
    //endregion

    //region constructor
    public SortMenuView(Context context) {
        super(context, MenuType.SORT);
        init();
    }
    //endregion

    //region setup
    private void init() {
        mc.setOnSortMenuItemSelectListener(this);
        addButtons();
    }
    //endregion

    //region helper
    private void addButtons() {
        acceptButton = newLabeledIconButton(R.drawable.ic_accept, "Accept");
        acceptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedType.childOf(MenuType.SORT) && selectedView instanceof SortMenuItemView) {
                    mc.onSortMenuItemChange(selectedType, ((SortMenuItemView) selectedView).isAscendingOrder());
                    mc.handleCloseMenu();
                }
            }
        });
        acceptButton.setEnabled(false);
        declineButton = newLabeledIconButton(R.drawable.ic_cancel, "Cancel");
        declineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Restore previous selected view + order
                mc.handleCloseMenu();
            }
        });


        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.addView(acceptButton);
        container.addView(declineButton);
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(container);
    }

    private LabeledIconButton newLabeledIconButton(int resId, String text) {
        LabeledIconButton button = new LabeledIconButton(getContext());
        button.setIconResource(resId);
        button.setLabel(text);
        button.setBackgroundResource(R.color.transparent);
        button.setLayoutParams(LayoutUtils.newLinearLayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        return button;
    }
    //endregion

    //region override
    @Override
    public void onMenuItemSelect(MenuItemType menuItemType) {
        super.onMenuItemSelect(menuItemType);
        acceptButton.setEnabled(true);
    }
    //endregion
}