package com.cloudclass;

public class StudentCheckinHistoryItem {
    private String time;
    private String status;

    public StudentCheckinHistoryItem(String time, String status) {
        this.time = time;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
