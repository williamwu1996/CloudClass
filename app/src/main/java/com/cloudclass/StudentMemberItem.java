package com.cloudclass;

public class StudentMemberItem {
    private int imageid;
    private String name;

    public StudentMemberItem(int imageid, String name) {
        this.imageid = imageid;
        this.name = name;
    }

    public int getImageid() {
        return imageid;
    }

    public String getName() {
        return name;
    }
}
