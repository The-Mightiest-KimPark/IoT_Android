package com.example.smartrefri.model;

public class Icon {
    public int url;
    public int all_grocery_id;
    public String name;

    public Icon(int url, int all_grocery_id, String name) {
        this.url = url;
        this.all_grocery_id = all_grocery_id;
        this.name = name;
    }


    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public int getAll_grocery_id() {
        return all_grocery_id;
    }

    public void setAll_grocery_id(int all_grocery_id) {
        this.all_grocery_id = all_grocery_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
