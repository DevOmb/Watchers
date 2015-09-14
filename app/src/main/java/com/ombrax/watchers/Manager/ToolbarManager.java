package com.ombrax.watchers.Manager;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

/**
 * Created by Ombrax on 24/08/2015.
 */
public class ToolbarManager {

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
    }
    //endregion

    //region method
    public void setExpandingTitle(String title){
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle(title);
            System.out.println("Toolbar Title: "+title);
        }
    }

    public void setMainActionItemDrawable(int resource){
        if(toolbar != null){
            toolbar.setNavigationIcon(resource);
        }
    }

    public void attachToActivity(AppCompatActivity activity){
        if(activity != null && toolbar != null){
            activity.setSupportActionBar(toolbar);
        }
    }

    public void setToolbarExpanded(boolean expand){
        if(appBarLayout != null){
            appBarLayout.setExpanded(expand);
        }
    }
    //endregion
}
