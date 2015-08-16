package com.ombrax.watchers.Views.Menu;

import android.content.Context;
import android.view.View;

import com.ombrax.watchers.Enums.MenuItemType;

/**
 * Created by Ombrax on 25/07/2015.
 */
public class MainMenuItemView extends MenuItemView{

    //region constructor
    public MainMenuItemView(Context context) {
        super(context);
        init();
    }
    //endregion

    //region setup
    private void init() {
       setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               mc.onMainMenuItemClick((MenuItemType) getTag());
           }
       });
    }
    //endregion
}
