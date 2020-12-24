package com.example.smartrefri.model;

import java.io.Serializable;

public class Follow implements Serializable {
    public String email;//친구이메일
    public String url;//친구 냉장고 사진 url
    public String reg_date;
    public String name;
    public String img_url;
    public int sex;
    public boolean read;

    public String following_user_id;


    public Follow(String email, String following_user_id){
        this.email = email;
        this.following_user_id = following_user_id;
    }

    public Follow(String email, String url, String reg_date, String name, String img_url, int sex, boolean read) {
        this.email = email;
        this.url = url;
        this.reg_date = reg_date;
        this.name = name;
        this.img_url = img_url;
        this.sex = sex;
        this.read = read;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getFollowing_user_id() {
        return following_user_id;
    }

    public void setFollowing_user_id(String following_user_id) {
        this.following_user_id = following_user_id;
    }
}
