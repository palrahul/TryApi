package com.watbots.tryapi.model;

import java.util.List;

public class ItemListResponse {
    public final List<Item> items;

    public ItemListResponse(List<Item> items) {
        this.items = items;
    }
}
