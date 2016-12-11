package com.watbots.tryapi.model;


import io.realm.RealmObject;

public class Item extends RealmObject{
    public int code;
    public String date;
    public String day;
    public int high;
    public int low;
    public String text;

}
