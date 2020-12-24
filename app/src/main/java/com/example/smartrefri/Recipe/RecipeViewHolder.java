package com.example.smartrefri.Recipe;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    ImageView itemfood_thumb_iv;
    TextView food_name_tv, food_ingredient_tv;
    LinearLayout recipe_item;


    public RecipeViewHolder(View recipeItemView) {
        super(recipeItemView);
        recipe_item = recipeItemView.findViewById(R.id.recipe_list_item);
        itemfood_thumb_iv = recipeItemView.findViewById(R.id.itemfood_thumb_iv);

        food_name_tv = recipeItemView.findViewById(R.id.food_name_tv);
        food_ingredient_tv = recipeItemView.findViewById(R.id.ingredient_tv);
    }
}
