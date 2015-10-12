package com.ombrax.watchers.Manager;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ombrax on 24/08/2015.
 */
public class ToolbarManager {

    //region inner field
    private boolean initialized;
    private Map<Integer, View> toolbarItems;
    private boolean isToolbarExpanded;
    //endregion

    //region variable
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    //endregion

    //region singleton
    private static ToolbarManager instance = new ToolbarManager();

    public static ToolbarManager getInstance() {
        return instance;
    }

    private ToolbarManager() {
    }
    //endregion

    //region setup
    public void initialize(CollapsingToolbarLayout collapsingToolbarLayout, AppBarLayout appBarLayout, Toolbar toolbar) {
        this.collapsingToolbarLayout = collapsingToolbarLayout;
        this.appBarLayout = appBarLayout;
        this.toolbar = toolbar;
        this.toolbarItems = new HashMap<>();
        initialized = true;
    }
    //endregion

    //region method
    public void setExpandingTitleOnTransition(String title, boolean expand) {
        checkInit();

        collapsingToolbarLayout.setTitleEnabled(isToolbarExpanded || expand);
        isToolbarExpanded = expand;
        appBarLayout.setExpanded(expand);

        if (collapsingToolbarLayout.isTitleEnabled()) {
            collapsingToolbarLayout.setTitle(title);
        } else {
            toolbar.setTitle(title);
        }
    }

    public void setMainActionItemDrawable(int resource) {
        checkInit();
        toolbar.setNavigationIcon(resource);
    }

    public void attachToActivity(AppCompatActivity activity) {
        checkInit();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    public void setToolbarItemVisibility(int id, boolean visible) {
        checkInit();
        View item = toolbarItems.get(id);
        if (item == null && toolbar != null) {
            item = toolbar.findViewById(id);
            if (item == null) {
                return;
            }
            toolbarItems.put(id, item);
        }
        item.setEnabled(visible);
        item.setAlpha(visible ? 1 : 0);
    }

    //endregion

    //region helper
    private void checkInit() {
        if (!initialized) {
            throw new NullPointerException(ToolbarManager.class.getSimpleName() + " has not been initialized");
        }
    }
    //endregion
}
