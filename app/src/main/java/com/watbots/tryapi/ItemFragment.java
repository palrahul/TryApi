package com.watbots.tryapi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.watbots.tryapi.api.ApiService;
import com.watbots.tryapi.api.Results;
import com.watbots.tryapi.api.ServiceGenerator;
import com.watbots.tryapi.dummy.DummyContent;
import com.watbots.tryapi.dummy.DummyContent.DummyItem;
import com.watbots.tryapi.model.Item;
import com.watbots.tryapi.model.ItemListResponse;
import com.watbots.tryapi.util.Funcs;
import com.watbots.tryapi.util.ResultToItemList;

import java.util.List;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter itemsAdapter;
    private ApiService apiService;
    private List<Item> items;


    Picasso picasso ;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        picasso = Picasso.with(getActivity());
        itemsAdapter = new MyItemRecyclerViewAdapter(picasso, mListener);
        apiService = ServiceGenerator.createService(ApiService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            itemsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override public void onChanged() {
                    //TODO handle case when empty , show loading
                }

             });
            recyclerView.setAdapter(itemsAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void onResume() {
        super.onResume();

        Observable<Result<List<Item>>> result =
                apiService.getRestaurantList(37.422740, -122.139956)
                        .subscribeOn(Schedulers.io())
                        .doOnError(some -> Log.e("ItemFragment", "API FAIL !!! :", some))
                        .share();
        result
                .filter(Results.isSuccessful())
                .map(ResultToItemList.instance())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemsAdapter);

        result //
                .filter(Funcs.not(Results.isSuccessful())) //
                .subscribe(some -> {
                    Log.e("ItemFragment", "Unable to load results from API !!");
                });

        //TODO add doUntil with a subject for finishing subsciptions and emit on it from onPause
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Item item);
    }
}
