package com.samsung.android.sdk.accessory.example.helloaccessory.consumer.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BPM {
    public String rate;
    public Date date;
    public String uid;
    public String key;

    public BPM(){

    }
    public BPM(String uid, Date date, String rate, String key){
        this.uid=uid;
        this.date=date;
        this.rate=rate;
        this.key=key;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("date", date);
        result.put("rate", rate);
        result.put("key",key);
        return result;
    }
}
