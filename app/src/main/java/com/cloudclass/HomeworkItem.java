package com.cloudclass;

public class HomeworkItem {
    String title;
    String id;
    String value;
    String profile;

    public HomeworkItem(String title, String id, String value, String profile) {
        this.title = title;
        this.id = id;
        this.value = value;
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
