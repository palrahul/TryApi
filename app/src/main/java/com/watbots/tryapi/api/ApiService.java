package com.watbots.tryapi.api;

import com.watbots.tryapi.model.Item;
import com.watbots.tryapi.model.Token;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    @GET("/v2/restaurant/")
    Observable<Result<List<Item>>> getRestaurantList(
            @Query("lat") double latitude,
            @Query("lng") double longitude
    );

    @GET("/v2/restaurant/{restaurantId}")
    Observable<Result<Item>> getRestaurant(
            @Path("restaurantId") String restaurantId
    );

    @FormUrlEncoded
    @POST("/v2/auth/token/")
    Observable<Token> getAuthToken(
        @Field("email") String email, @Field("password") String password);

}
