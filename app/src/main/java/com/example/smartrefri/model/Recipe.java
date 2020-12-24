package com.example.smartrefri.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
   public int id;
   public String email;
   public int all_recipe_id;
   public String name;
   public String ingredient;
   public String ingredient_name;
   public String seasoning;
   public String seasoning_name;
   public String howto;
   public String purpose;
   public int views;
   public String img;
   public int recipe_num;

   public Recipe recipe1 ;
    public Recipe(int id, String email, int all_recipe_id, String name, String ingredient, String ingredient_name, String seasoning, String seasoning_name, String howto, String purpose, int views, String img, int recipe_num) {
        this.id = id;
        this.email = email;
        this.all_recipe_id = all_recipe_id;
        this.name = name;
        this.ingredient = ingredient;
        this.ingredient_name = ingredient_name;
        this.seasoning = seasoning;
        this.seasoning_name = seasoning_name;
        this.howto = howto;
        this.purpose = purpose;
        this.views = views;
        this.img = img;
        this.recipe_num = recipe_num;
    }

    public Recipe() {

    }

    public Recipe(Recipe recipe) {
        this.recipe1 =recipe;
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

    public int getAll_recipe_id() {
        return all_recipe_id;
    }

    public void setAll_recipe_id(int all_recipe_id) {
        this.all_recipe_id = all_recipe_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getSeasoning() {
        return seasoning;
    }

    public void setSeasoning(String seasoning) {
        this.seasoning = seasoning;
    }

    public String getSeasoning_name() {
        return seasoning_name;
    }

    public void setSeasoning_name(String seasoning_name) {
        this.seasoning_name = seasoning_name;
    }

    public String getHowto() {
        return howto;
    }

    public void setHowto(String howto) {
        this.howto = howto;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRecipe_num() {
        return recipe_num;
    }

    public void setRecipe_num(int recipe_num) {
        this.recipe_num = recipe_num;
    }
}
