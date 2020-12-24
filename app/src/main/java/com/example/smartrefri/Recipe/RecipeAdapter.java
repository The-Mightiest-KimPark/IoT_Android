package com.example.smartrefri.Recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.model.Recipe;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    ArrayList<Recipe> recipeList;
    private Context context;
    Intent intent;
    Boolean like=true;
    Fragment recipe_detail = new FragmentRecipeDetail();
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    NetworkService networkService;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList,FragmentManager fragmentManager) {
        this.context = context;
        this.recipeList = recipeArrayList;
        this.fragmentManager = fragmentManager;

    }
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeItemView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(recipeItemView);
        networkService = ApplicationController.getInstance().getNetworkService();
        return recipeViewHolder;
    }
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        Glide.with(context)
                .load(recipeList.get(position).getImg())
                .into(holder.itemfood_thumb_iv);
        holder.food_name_tv.setText(recipeList.get(position).getName());
        holder.food_ingredient_tv.setText(recipeList.get(position).getIngredient());

        checkLike(recipeList.get(position),holder);

        holder.recipe_item.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.e("너 정체가뭐야",recipeList.get(position).getName());
                Recipe detailRecipe = new Recipe();
                detailRecipe = recipeList.get(position);

                Log.e("올바른 값으로 찍히긴 한거니.?",detailRecipe.getName());

                Bundle bundle = new Bundle();
                bundle.putSerializable("RecipeValue",detailRecipe);
                recipe_detail.setArguments(bundle);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,recipe_detail);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });




    }



    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void checkLike(Recipe recipe,RecipeViewHolder holder){
//        Call<ArrayList<Recipe>> getLikeRecipeList = networkService.getLikeRecipeList(ApplicationController.user_id);
//        getLikeRecipeList.enqueue(new Callback<ArrayList<Recipe>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
//                ArrayList<Recipe> likeRecipeList = new ArrayList<Recipe>();
//                likeRecipeList = response.body();
//
//                for(int i = 0; i<likeRecipeList.size();i++){
//                    if(likeRecipeList.get(i).getAll_recipe_id() == recipe.getAll_recipe_id()){
//
//                        holder.like_recipe_btn.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
//                        holder.like_recipe_btn.setImageResource(R.drawable.heart);
//
//                    }else{
//                        holder.like_recipe_btn.setVisibility(View.GONE);
//                    }
//                }
//                Log.e("즐겨찾기 레시피 잘 불러졌나요?",String.valueOf(response.code()));
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
//
//            }
//        });

    }
}
