package com.cloudclass;

public class StudentMemberItem {
    private String imageid;
    private String name;

    public StudentMemberItem(String imageid, String name) {
        this.imageid = imageid;
        this.name = name;
    }

    public String getImageid() {
        return imageid;
    }

    public String getName() {
        return name;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
