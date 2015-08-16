package com.ombrax.watchers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ombrax.watchers.Enums.MenuType;
import com.ombrax.watchers.Models.MenuItem;
import com.ombrax.watchers.Manager.MenuManager;
import com.ombrax.watchers.Views.Menu.MainMenuItemView;
import com.ombrax.watchers.Views.Menu.MenuItemView;
import com.ombrax.watchers.Views.Menu.SortMenuItemView;

import java.util.List;

/**
 * Created by Ombrax on 7/08/2015.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    //region variable
    private Context context;
    private List<MenuItem> list;
    private MenuType menuType;
    //endregion

    //region constructor
    public MenuAdapter(Context context, MenuType menuType) {
        this.context = context;
        this.list = MenuManager.getInstance().getAll(menuType);
        this.menuType = menuType;
    }
    //endregion

    //region override
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (menuType) {
            case MAIN:
                return new MenuViewHolder(new MainMenuItemView(context));
            case SORT:
                return new MenuViewHolder(new SortMenuItemView(context));
        }
        return new MenuViewHolder(new MenuItemView(context));
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        MenuItemView menuItemView = (MenuItemView) holder.itemView;
        menuItemView.setModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //endregion

    //region viewholder
    public class MenuViewHolder extends RecyclerView.ViewHolder {

        public MenuViewHolder(MenuItemView menuItemView) {
            super(menuItemView);
        }
    }
    //endregion
}
