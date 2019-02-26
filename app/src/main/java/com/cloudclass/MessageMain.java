package com.cloudclass;

public class MessageMain {

    private int imageid;
    private String fromclass;
    private String time;
    private String name;
    private String firstcontent;

    public MessageMain(int imageid, String fromclass, String time, String name, String firstcontent) {
        this.imageid = imageid;
        this.fromclass = fromclass;
        this.time = time;
        this.name = name;
        this.firstcontent = firstcontent;
    }

    public int getImageid() {
        return imageid;
    }

    public String getFromclass() {
        return fromclass;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getFirstcontent() {
        return firstcontent;
    }
}
