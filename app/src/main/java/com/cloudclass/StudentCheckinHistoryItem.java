package com.cloudclass;

public class StudentCheckinHistoryItem {
    private String time;
    private String status;
    private String id;

    public StudentCheckinHistoryItem(String time, String status, String id) {
        this.time = time;
        this.status = status;
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
