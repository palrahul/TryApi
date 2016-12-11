package com.watbots.tryapi.util;
import com.watbots.tryapi.model.Weather;
import com.watbots.tryapi.model.WeatherResponse;

import java.util.Collections;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;


public final class ResultToWeatherList implements Func1<Result<WeatherResponse>, List<Weather>> {
    private static volatile ResultToWeatherList instance;

    public static ResultToWeatherList instance() {
        if (instance == null) {
            instance = new ResultToWeatherList();
        }
        return instance;
    }

    @Override public List<Weather> call(Result<WeatherResponse> result) {
        List<Weather> items = null;
        WeatherResponse response = result.response().body();
        items = response.query.results.channel.item.forecast;
        return items == null //
                ? Collections.<Weather>emptyList() //
                : items;
    }
}

