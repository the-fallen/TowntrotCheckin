package com.towntrot.checkin;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Post_check implements Serializable {

    @SerializedName("success")
    public Boolean success;

    @SerializedName("authentication")
    public Boolean authentication;

}