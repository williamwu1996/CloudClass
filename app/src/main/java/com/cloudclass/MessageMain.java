package com.cloudclass;

public class MessageMain {

    private String imageid;
    private String fromclass;
    private String time;
    private String name;
    private String firstcontent;

    public MessageMain(String imageid, String fromclass, String time, String name, String firstcontent) {
        this.imageid = imageid;
        this.fromclass = fromclass;
        this.time = time;
        this.name = name;
        this.firstcontent = firstcontent;
    }

    public String getImageid() {
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

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public void setFromclass(String fromclass) {
        this.fromclass = fromclass;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstcontent(String firstcontent) {
        this.firstcontent = firstcontent;
    }
}
