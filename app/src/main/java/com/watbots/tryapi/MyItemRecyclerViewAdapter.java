package com.watbots.tryapi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.watbots.tryapi.ItemFragment.OnListFragmentInteractionListener;
import com.watbots.tryapi.dummy.DummyContent.DummyItem;
import com.watbots.tryapi.model.Item;
import com.watbots.tryapi.ui.ListItemView;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>
        implements Action1<List<Item>> {

    private List<Item> items = Collections.emptyList();
    private final OnListFragmentInteractionListener mListener;
    private Picasso picasso;

    public MyItemRecyclerViewAdapter(Picasso picasso, OnListFragmentInteractionListener listener) {
        this.picasso = picasso;
        mListener = listener;
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
        holder.bindTo(items.get(position));
//        holder.mItem = items.get(position);
//        holder.mIdView.setText(items.get(position).name);
//        holder.mContentView.setText(items.get(position).description);
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemView itemView;

        public ViewHolder(ListItemView itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(v -> {
                Item item = items.get(getAdapterPosition());
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
            });
        }

        public void bindTo(Item item) {
            itemView.bindTo(item, picasso);
        }

//        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
//        public Item mItem;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
//            mContentView = (TextView) view.findViewById(R.id.content);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
