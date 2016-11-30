package com.watbots.tryapi;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.watbots.tryapi.ItemFragment.OnListFragmentInteractionListener;
import com.watbots.tryapi.db.RealmRecyclerViewAdapter;
import com.watbots.tryapi.dummy.DummyContent.DummyItem;
import com.watbots.tryapi.model.Item;
import com.watbots.tryapi.ui.ListItemView;

import java.util.Collections;
import java.util.List;

import io.realm.OrderedRealmCollection;
import rx.functions.Action1;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyItemRecyclerViewAdapter extends RealmRecyclerViewAdapter<Item, MyItemRecyclerViewAdapter.ViewHolder>
        implements Action1<List<Item>> {

    private List<Item> items = Collections.emptyList();
    private final OnListFragmentInteractionListener mListener;
    private Picasso picasso;
    private OrderedRealmCollection<Item> savedItems;
    private boolean showSaved = false;

    public MyItemRecyclerViewAdapter(Picasso picasso, OnListFragmentInteractionListener listener,
                                     OrderedRealmCollection<Item> data, boolean showSaved) {
        super(data, false);
        this.picasso = picasso;
        mListener = listener;
        savedItems = data;
        this.showSaved = showSaved;
    }

    @Override public void call(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemView view = (ListItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(showSaved ) {
            if(savedItems.size() != 0 && position < savedItems.size()) {
                holder.bindTo(getData().get(position));
            }
        } else {
            holder.bindTo(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(showSaved ) {
            return savedItems.size();
        } else {
            return items.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemView itemView;

        public ViewHolder(ListItemView itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnLongClickListener(view -> {
                Log.d("some", "onLongClick");
                Item item = items.get(getAdapterPosition());
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
                return false;
            });
        }

        public void bindTo(Item item) {
            itemView.bindTo(item, picasso);
        }

    }
}
