package com.cloudclass;

public class ClassMain {

    private String imageid;
    private String code;
    private String classname;
    private String course;
    private String teacher;
    private String iscreater;
    private String profile;


    public ClassMain(String imageid, String code, String classname, String course, String teacher, String iscreater, String profile) {
        this.imageid = imageid;
        this.code = code;
        this.classname = classname;
        this.course = course;
        this.teacher = teacher;
        this.iscreater = iscreater;
        this.profile = profile;
    }

    public String getImageid() {
        return imageid;
    }

    public String getCode() {
        return code;
    }

    public String getClassname() {
        return classname;
    }

    public String getCourse() {
        return course;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getIscreater() {
        return iscreater;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setIscreater(String iscreater) {
        this.iscreater = iscreater;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
