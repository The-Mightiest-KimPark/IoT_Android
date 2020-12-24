package com.example.smartrefri.model;

import java.io.Serializable;

public class Fridge implements Serializable {

    public String fridge_number;
    public String email;

    public Fridge(String fridge_number, String email) {
        this.fridge_number = fridge_number;
        this.email = email;
    }

    public String getFridge_number() {
        return fridge_number;
    }

    public void setFridge_number(String fridge_number) {
        this.fridge_number = fridge_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
