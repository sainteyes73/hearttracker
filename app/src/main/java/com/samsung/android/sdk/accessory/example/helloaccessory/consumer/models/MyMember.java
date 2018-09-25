package com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MyMember {
    public String uid;
    public String author;
    public String name;
    public String phonenumber;
    public String key;

    public MyMember(){

    }
    public MyMember(String uid, String author, String name, String phonenumber,String key){
        this.uid=uid;
        this.name=name;
        this.phonenumber=phonenumber;
        this.author=author;
        this.key=key;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("name", name);
        result.put("phonenumber", phonenumber);
        result.put("key",key);
        return result;
    }
}

