package com.towntrot.checkin;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User_details implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    public String getID(){
        return id;
    }

    public String getKey(){
        return key;
    }
}