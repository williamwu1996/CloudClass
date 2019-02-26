package com.cloudclass;

public class StudentResourceItem {
    private int imageid;
    private String name;
    private String opentime;

    public StudentResourceItem(int imageid, String name, String opentime) {
        this.imageid = imageid;
        this.name = name;
        this.opentime = opentime;
    }

    public int getImageid() {
        return imageid;
    }

    public String getName() {
        return name;
    }

    public String getOpentime() {
        return opentime;
    }
}
