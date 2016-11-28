package com.watbots.tryapi.util;

import com.watbots.tryapi.model.Item;
import com.watbots.tryapi.model.ItemListResponse;

import java.util.Collections;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;


public final class ResultToItemList implements Func1<Result<List<Item>>, List<Item>> {
    private static volatile ResultToItemList instance;

    public static ResultToItemList instance() {
        if (instance == null) {
            instance = new ResultToItemList();
        }
        return instance;
    }

    @Override public List<Item> call(Result<List<Item>> result) {
        List<Item> items = result.response().body();
        return items == null //
                ? Collections.<Item>emptyList() //
                : items;
    }
}

