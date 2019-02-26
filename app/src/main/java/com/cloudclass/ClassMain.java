package com.cloudclass;

public class ClassMain {

    private int imageid;
    private String code;
    private String classname;
    private String course;
    private String teacher;

    public ClassMain(int imageid, String code, String classname, String course, String teacher) {
        this.imageid = imageid;
        this.code = code;
        this.classname = classname;
        this.course = course;
        this.teacher = teacher;
    }

    public int getImageid() {
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
}
