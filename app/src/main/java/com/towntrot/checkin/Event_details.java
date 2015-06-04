package com.towntrot.checkin;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Event_details implements Serializable {

    @SerializedName("results")
    public about_event[] event_det;

    @SerializedName("authentication")
    public Boolean authentication;

}

