package com.mogal.classes;

import java.util.Date;

public class User {
    private String nickname;
    private String profile_picture;
    private String uid;
    private Date joinedOn;

    public User(String nickname, String profile_picture, String uid, Date joinedOn) {
        this.nickname = nickname;
        this.profile_picture = profile_picture;
        this.uid = uid;
        this.joinedOn = joinedOn;
    }

    public User() {}

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(Date joinedOn) {
        this.joinedOn = joinedOn;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", uid='" + uid + '\'' +
                ", joinedOn=" + joinedOn +
                '}';
    }
}
