package com.mogal.classes;

import java.util.Date;

public class Article {

    private String name;
    private String body;
    private String picture;
    private String posterUID;
    private String ID;
    private Date time;
    private String country;

    public Article(String name, String body, String picture, String posterUID, String ID, Date time, String country){
        this.name = name;
        this.body = body;
        this.picture = picture;
        this.posterUID = posterUID;
        this.ID = ID;
        this.time = time;
        this.country = country;
    }

    public Article(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPosterUID() {
        return posterUID;
    }

    public void setPosterUID(String posterUID) {
        this.posterUID = posterUID;
    }

    public void setPoster(String posterUID) {
        this.posterUID = posterUID;
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


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
