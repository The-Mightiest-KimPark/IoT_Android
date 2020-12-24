package com.example.smartrefri.model;

//알람 정보
public class Alarm {
    public int id;
    public String email;
    public int all_grocery_id;
    public int count;
    public String name;


    public Alarm(){

    }

    //식재료 알림 삭제

    public Alarm(String email, int all_grocery_id) {
        this.email = email;
        this.all_grocery_id = all_grocery_id;
    }

    //식재료 알림 등록
    public Alarm(String email, int all_grocery_id, int count) {
        this.email = email;
        this.all_grocery_id = all_grocery_id;
        this.count = count;
    }

    public Alarm(int all_grocery_id, int count, String name) {
        this.all_grocery_id = all_grocery_id;
        this.count = count;
        this.name = name;
    }

    //식재료 알림 조회
    public Alarm(int id, String email, int all_grocery_id, int count) {
        this.id = id;
        this.email = email;
        this.all_grocery_id = all_grocery_id;
        this.count = count;
    }

    public Alarm(int id, String email, int all_grocery_id, int count, String name) {
        this.id = id;
        this.email = email;
        this.all_grocery_id = all_grocery_id;
        this.count = count;
        this.name = name;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int _id) {
        this.id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAll_grocery_id() {
        return all_grocery_id;
    }

    public void setAll_grocery_id(int all_grocery_id) {
        this.all_grocery_id = all_grocery_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
