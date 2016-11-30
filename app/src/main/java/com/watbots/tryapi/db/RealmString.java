package com.watbots.tryapi.db;

import io.realm.RealmObject;

public class RealmString extends RealmObject{
    private String value;

    public RealmString() {

    }

    public RealmString(String val) {
        this.value = val;
    }
}
