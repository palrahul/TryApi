package com.watbots.tryapi.model;

import com.watbots.tryapi.db.RealmString;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Item extends RealmObject{

    public Item() {

    }


    public boolean is_time_surging;
    public String max_order_size;
    public int preference_level;
    public int delivery_fee;
    public int max_composite_score;
    public int id;
    public RealmList<Menus> menus;
    public int composite_score;
    public String status_type;
    public boolean is_only_catering;
    public String status;
    public String asap_time;
    public String description;
    public Business business;
    public RealmList<RealmString> tags;
    public int yelp_review_count;
    public float yelp_rating;
    public int business_id;
    public String cover_img_url;
    public String header_img_url;
    public Address address;
    public int price_range;
    public String slug;
    public String name;
    public String url;
    public String service_rate;
    public String promotion;
    public String featured_category_description;





}
