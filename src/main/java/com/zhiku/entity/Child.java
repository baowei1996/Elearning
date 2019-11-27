package com.zhiku.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

public class Child {
    private int sid;
    private String section_seq;
    private String section_name;
    private int level;
    private ArrayList<Document> contents;
    private ArrayList<Child> sub;

    public ArrayList<Document> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Document> contents) {
        this.contents = contents;
    }

    public ArrayList<Child> getSub() {
        return sub;
    }

    public void setSub(ArrayList<Child> sub) {
        this.sub = sub;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSection_seq() {
        return section_seq;
    }

    public void setSection_seq(String section_seq) {
        this.section_seq = section_seq;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}