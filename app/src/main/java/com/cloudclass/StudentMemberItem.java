package com.cloudclass;

public class StudentMemberItem {
    private String imageid;
    private String name;
    private String email;

    public StudentMemberItem(String imageid, String name, String email) {
        this.imageid = imageid;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
