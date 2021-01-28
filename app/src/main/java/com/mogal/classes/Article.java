package com.mogal.classes;

import java.util.Date;

public class Article {

    private String name;
    private String description;
    private String picture;
    private User poster;
    private String ID;
    private Date time;

    public Article(String name, String description, String picture, User poster, String ID, Date time){
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.poster = poster;
        this.ID = ID;
        this.time = time;
    }

    public Article(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
