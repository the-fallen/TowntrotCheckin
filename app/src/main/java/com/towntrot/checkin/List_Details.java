package com.towntrot.checkin;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class List_Details implements Serializable {

    @SerializedName("bookings")
    public bookings book_det;

    @SerializedName("results")
    public about_list[] list_det;

    @SerializedName("authentication")
    public Boolean authentication;

}

