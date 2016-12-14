package com.watbots.tryapi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.watbots.tryapi.api.ApiService;
import com.watbots.tryapi.api.Results;
import com.watbots.tryapi.api.ServiceGenerator;
import com.watbots.tryapi.model.Item;
import com.watbots.tryapi.util.Funcs;
import com.watbots.tryapi.util.ResultToItemList;

import java.util.List;

import butterknife.BindDimen;
import io.realm.Realm;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    private static final String SHOW_SAVED = "show-saved";
    private OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter itemsAdapter;
    private ApiService unauthApiService;
    private ApiService authApiService;

    private List<Item> items;
    private Picasso picasso ;
    private Realm realm;
    private boolean showSaved = false;

    @BindDimen(R.dimen.divider_padding_start) float dividerPaddingStart;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {

    }

    public static ItemFragment newInstance(boolean showSaved) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_SAVED, showSaved);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            showSaved = getArguments().getBoolean(SHOW_SAVED);
        }
        picasso = Picasso.with(getActivity());
        realm = Realm.getDefaultInstance();
        itemsAdapter = new MyItemRecyclerViewAdapter(picasso, mListener,
                realm.where(Item.class).findAllAsync(), showSaved);
        unauthApiService = ServiceGenerator.createService(ApiService.class, false);
        authApiService = ServiceGenerator.createService(ApiService.class, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            itemsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override public void onChanged() {
                    //TODO handle case when empty , show loading
                }

             });
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
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

        if(ServiceGenerator.getToken() == null) {
            unauthApiService.getAuthToken("some@xyz.com", "somepass")
                    .subscribeOn(Schedulers.io())
                    .take(1)
                    .doOnError(some -> Log.e("ItemFragment", some.getLocalizedMessage()))
                    .subscribe(token -> {
                        Log.d("ItemFrag", token.token);
                        ServiceGenerator.setToken(token.token);

                        //do something after login succeeds

                    });
        }

        Observable<Result<List<Item>>> result =
                unauthApiService.getRestaurantList(37.422740, -122.139956)
                        .subscribeOn(Schedulers.io())
                        //.take(1)
                        .doOnError(some -> Log.e("ItemFragment", "API FAIL !!! :", some))
                        .share();
        result
                .filter(Results.isSuccessful())
                .map(ResultToItemList.instance())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(some ->
                        itemsAdapter.call(some));

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
