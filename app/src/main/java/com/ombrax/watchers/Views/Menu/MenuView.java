package com.ombrax.watchers.Views.Menu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ombrax.watchers.Adapters.MenuAdapter;
import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Enums.MenuItemType;
import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Interfaces.Command;
import com.ombrax.watchers.Interfaces.IOnMenuItemSelectListener;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;
import com.ombrax.watchers.Views.Other.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ombrax on 7/08/2015.
 */
public class MenuView extends LinearLayout implements RecyclerView.OnChildAttachStateChangeListener, IOnMenuItemSelectListener {

    //region declaration
    //region constant
    public static final int BACKGROUND = R.color.dark_dark_grey;
    //endregion

    //region inner field
    private List<Command> deferredCommands = new ArrayList<>();
    private boolean contentLoaded;
    private int viewsAttached;

    protected MenuController mc;

    protected View selectedView;
    protected MenuItemType selectedType;

    protected RecyclerView recyclerView;
    protected MenuAdapter menuAdapter;
    protected LinearLayoutManager layoutManager;

    protected MenuType menuType;
    //endregion
    //endregion

    //region constructor
    public MenuView(Context context, MenuType menuType) {
        super(context);
        this.menuType = menuType;
        init();
    }
    //endregion

    //region setup
    private void init() {
        mc = MenuController.getInstance();
        setAttributes();
        addMenuList();
    }
    //endregion

    //region method
    public boolean hasNewSelection(MenuItemType menuItemType) {
        return selectedType != menuItemType;
    }
    //endregion

    //region helper
    private void setAttributes() {
        setOrientation(VERTICAL);
        setBackgroundResource(BACKGROUND);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addMenuList() {
        Context context = getContext();
        menuAdapter = new MenuAdapter(context, menuType);
        layoutManager = new LinearLayoutManager(context);
        recyclerView = new RecyclerView(context);
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(context.getResources().getDimensionPixelSize(R.dimen.divider)));
        recyclerView.addOnChildAttachStateChangeListener(this);
        addView(recyclerView, LayoutUtils.newLinearLayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
    }

    protected View findViewByMenuItemType(MenuItemType menuItemType) {
        return recyclerView.findViewWithTag(menuItemType);
    }

    protected void waitForRecyclerViewContent(Command command) {
        if (contentLoaded) {
            command.execute();
        } else {
            deferredCommands.add(command);
        }
    }
    //endregion

    //region interface implementation
    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (++viewsAttached == menuAdapter.getItemCount()) {
            contentLoaded = true;
            for (Command command : deferredCommands) {
                command.execute();
            }
            deferredCommands.clear();
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
    }

    @Override
    public void onMenuItemSelect(final MenuItemType menuItemType) {
        if (hasNewSelection(menuItemType)) {
            selectedType = menuItemType;
            if (selectedView != null) {
                selectedView.setSelected(false);
            }
            waitForRecyclerViewContent(new Command() {
                @Override
                public void execute() {
                    selectedView = findViewByMenuItemType(menuItemType);
                    selectedView.setSelected(true);
                }
            });
        }
    }
    //endregion
}
