package com.example.smartrefri.model;

import java.io.Serializable;

public class Like implements Serializable {

    public String recipe_id;

    public Like(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }
}
