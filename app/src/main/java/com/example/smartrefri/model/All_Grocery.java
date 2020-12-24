package com.example.smartrefri.model;

import java.io.Serializable;


//검색창과 알림식재료 검색창에 쓰일 전체 식재료 조회
public class All_Grocery implements Serializable {
    public int id;
    public String name;

    public All_Grocery(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public All_Grocery() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
