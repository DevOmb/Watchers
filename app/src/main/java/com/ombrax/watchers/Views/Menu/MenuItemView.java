package com.ombrax.watchers.Views.Menu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ombrax.watchers.Controllers.MenuController;
import com.ombrax.watchers.Models.MenuItem;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.LayoutUtils;

/**
 * Created by Ombrax on 25/07/2015.
 */
public class MenuItemView extends LinearLayout{

    //region inner field
    protected MenuController mc;
    protected TextView title;
    //endregion

    //region constructor
    public MenuItemView(Context context) {
        super(context);
        init();
    }
    //endregion

    //region setup
    private void init() {
        mc = MenuController.getInstance();
        setAttributes();
        setContentView();
    }
    //endregion

    //region method
    public void setModel(MenuItem menuItem) {
        title.setText(menuItem.name());
        title.setCompoundDrawablesWithIntrinsicBounds(menuItem.icon(), 0, 0, 0);
        //NOTE: Necessary to use method findViewWithTag on RecyclerView (parent)
        setTag(menuItem.type());
    }
    //endregion

    //region helper
    private void setAttributes(){
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void setContentView(){
        title = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.menu_item, null);
        addView(title, LayoutUtils.newLinearLayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
    }
    //endregion

    //region override
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1.0f : 0.5f);
    }
    //endregion
}
