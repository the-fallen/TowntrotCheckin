package com.towntrot.checkin;

public class bookings {
    public String[] bid;
    public String[] bname;
    public String[] bquantity;
    public String[] bprice;
    public String[] bt_start_time;
    public String[] bt_end_time;

    public String getBid(int i){
        return bid[i];
    }

    public String getBname(int i){
        return bname[i];
    }

    public String getBquantity(int i){
        return bquantity[i];
    }
    public String getBprice(int i){
        return bprice[i];
    }
    public String getBt_start_time(int i){
        return bt_start_time[i];
    }
    public String getBt_end_time(int i){
        return bt_end_time[i];
    }
}
