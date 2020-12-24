package com.example.smartrefri.model;

import java.io.Serializable;

public class Dvsensor implements Serializable {

    public int id;
    public String email;
    public String name;
    public int value;
    public String reg_date;

    public Dvsensor(int id, String email, String name, int value, String reg_date) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.value = value;
        this.reg_date = reg_date;
    }

    public Dvsensor() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
