package com.mogal.classes;

import java.util.Date;

public class Review {
    private String uid;
    private String nickname;
    private String profile_picture;
    private Date time;
    private String body;
    private boolean like;

    public Review(String uid, String nickname, String profile_picture, Date time, String body, boolean like) {
        this.uid = uid;
        this.nickname = nickname;
        this.profile_picture = profile_picture;
        this.time = time;
        this.body = body;
        this.like = like;
    }

    public Review() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
