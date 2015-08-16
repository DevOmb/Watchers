package com.ombrax.watchers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ombrax.watchers.Models.WatchModel;
import com.ombrax.watchers.Views.Card.WatchCardView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ombrax on 15/07/2015.
 */
public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchCardViewHolder> implements WatchCardView.ICardManager {

    //region variable
    private Context context;
    private List<WatchModel> watchModels;
    //endregion

    //region constructor
    public WatchListAdapter(Context context, List<WatchModel> watchModels) {
        this.context = context;
        this.watchModels = watchModels;
    }
    //endregion

    //region Description
    public void sort(Comparator<WatchModel> comparator){
        if(comparator != null) {
            Collections.sort(watchModels, comparator);
            notifyDataSetChanged();
            //notifyItemMoved(0, watchModels.size());
        }
    }
    //endregion

    //region override
    @Override
    public WatchCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WatchCardViewHolder(new WatchCardView(context, this));
    }

    @Override
    public void onBindViewHolder(WatchCardViewHolder vh, int i) {
        WatchCardView watchCardView = (WatchCardView) vh.itemView;
        watchCardView.setModel(watchModels.get(i));
    }

    @Override
    public int getItemCount() {
        return watchModels.size();
    }
    //endregion

    //region viewholder
    public class WatchCardViewHolder extends RecyclerView.ViewHolder {
        public WatchCardViewHolder(WatchCardView watchCardView) {
            super(watchCardView);
        }
    }
    //endregion

    //region interface implementation
    @Override
    public void remove(WatchModel watchModel) {
        int index = watchModels.indexOf(watchModel);
        watchModels.remove(index);
        notifyItemRemoved(index);
    }
    //endregion
}
