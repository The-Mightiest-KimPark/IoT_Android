package com.example.smartrefri.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Grocery implements Serializable {

    public int id;
    public String email;
    //전체식료품  id
    public int all_grocery_id;
    public String name;
    public int count;
    public String reg_date;
    //smart:1 custom:2
    public int gubun;

    public String expiration_date;

    public ArrayList<Double[]> coordinate = new ArrayList<Double[]>();

    public Grocery() {

    }

    public ArrayList<Double[]> getcoordinate() {
        return coordinate;
    }

    public void setCordiList(ArrayList<Double[]> cordiList) {
        this.coordinate = cordiList;
    }


    //커스텀 식재료 삭제
    public Grocery(String email, int all_grocery_id) {
        this.email = email;
        this.all_grocery_id = all_grocery_id;
    }

    //커스텀 식재료 추가
    public Grocery(String email, int all_grocery_id, String name, int count, String expiration_date) {
        this.email = email;
        this.all_grocery_id = all_grocery_id;
        this.name = name;
        this.count = count;
        this.expiration_date = expiration_date;
    }

    //나의 식재료 리스트 받아오기
    public Grocery(int id, String email, int grocery_id, String name, int count,String reg_date, int gubun, ArrayList<Double[]> cordiList) {
        this.id = id;
        this.email = email;
        this.all_grocery_id = grocery_id;
        this.name = name;
        this.count = count;
        this.reg_date = reg_date;
        this.gubun = gubun;
        this.coordinate = cordiList;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGrocery_id() {
        return all_grocery_id;
    }

    public void setGrocery_id(int grocery_id) {
        this.all_grocery_id = grocery_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public int getGubun() {
        return gubun;
    }

    public void setGubun(int gubun) {
        this.gubun = gubun;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
}
