package com.cloudclass;

public class CheckinMainItem {

    private String time;
    private String chid;

    public CheckinMainItem(String time, String chid) {
        this.time = time;
        this.chid = chid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChid() {
        return chid;
    }

    public void setChid(String chid) {
        this.chid = chid;
    }
}
