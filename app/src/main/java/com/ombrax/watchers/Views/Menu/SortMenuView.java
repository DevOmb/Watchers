package com.ombrax.watchers.Views.Menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ombrax.watchers.Enums.ActionSetting;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Interfaces.Command;
import com.ombrax.watchers.Interfaces.Observer.IOnSortMenuClosedListener;
import com.ombrax.watchers.Manager.SettingsManager;
import com.ombrax.watchers.Manager.SortingManager;
import com.ombrax.watchers.Models.SortModel;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Views.Button.LabeledIconButton;

/**
 * Created by Ombrax on 6/08/2015.
 */
public class SortMenuView extends MenuView implements IOnSortMenuClosedListener {

    //region inner field
    private LabeledIconButton acceptButton;
    private LabeledIconButton declineButton;

    private SortingManager sortingManager;
    private SortModel sortModel;
    private boolean changeOrderManual;
    private boolean declineButtonEnabledAction;
    //endregion

    //region constructor
    public SortMenuView(Context context) {
        super(context, MenuType.SORT);
        init();
    }
    //endregion

    //region setup
    private void init() {
        sortingManager = SortingManager.getInstance();
        sortModel = sortingManager.getSystemSort();
        mc.setOnSortMenuClosedListener(this);
        mc.setOnSortMenuItemSelectListener(this);
        waitForRecyclerViewContent(new Command() {
            @Override
            public void execute() {
                changeOrderManual = true;
                mc.onMenuItemSelect(sortModel.getSortType());
            }
        });
        addButtons();
    }
    //endregion

    //region helper
    private void addButtons() {
        acceptButton = newLabeledIconButton(R.drawable.ic_accept, "Accept");
        acceptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptSort(true);
            }
        });
        declineButton = newLabeledIconButton(R.drawable.ic_cancel, "Cancel");
        declineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                declineButtonEnabledAction = true;
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

    private void acceptSort(boolean handleClose) {
        boolean isAscending = ((SortMenuItemView) selectedView).isAscendingOrder();
        sortModel = new SortModel(selectedType, isAscending);
        sortingManager.updateSystemSort(sortModel);
        mc.onSortOrderChange(sortModel);
        if (handleClose) {
            mc.handleCloseMenu();
        }
    }

    private void declineSort(boolean handleClose) {
        changeOrderManual = true;
        mc.onMenuItemSelect(sortModel.getSortType());//NOTE: This calls the implementation in the parent first !
        if (handleClose) {
            mc.handleCloseMenu();
        }
    }
    //endregion

    //region interface implementation
    @Override
    public void onMenuItemSelect(MenuItemType menuItemType) {
        super.onMenuItemSelect(menuItemType);
        if (changeOrderManual) {
            changeOrderManual = false;
            ((SortMenuItemView) selectedView).setOrder(sortModel.isAscending());
        }
    }

    @Override
    public void onSortMenuClosed(boolean isUserEnabled) {
        if (isUserEnabled) {
            if (SettingsManager.getInstance().getOnSortMenuCloseAction() == ActionSetting.OnSortMenuClose.DECLINE) {
                declineSort(false);
            } else {
                acceptSort(false);
            }
        } else if(declineButtonEnabledAction){
            declineButtonEnabledAction = false;
            declineSort(false);
        }
    }
    //endregion
}