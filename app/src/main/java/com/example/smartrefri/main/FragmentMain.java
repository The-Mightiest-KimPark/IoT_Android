package com.example.smartrefri.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartrefri.R;
import com.example.smartrefri.Recipe.FragmentRecipeDetail;
import com.example.smartrefri.Recipe.FragmentRecipeList;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.FragmentGrocery;
import com.example.smartrefri.model.Dvsensor;
import com.example.smartrefri.model.Follow;
import com.example.smartrefri.model.Mode;
import com.example.smartrefri.model.Recipe;
import com.example.smartrefri.model.User;

import java.io.IOException;
import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;

import com.example.smartrefri.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    LinearLayoutManager layoutManager;
    LinearLayout food_detail_btn;
    ImageView food_list_btn, food_thumb_iv, mode_change_iv;
    TextView temp_tv, humi_tv, outing_state_tv, title,main_recipe_food_tv,main_recipe_ingredient_tv,follow_no_noti;
    Drawable alpha;
    ArrayList<User> storyUser;
    MainActivity activity;

    Recipe recipe;
    Intent intent;

    Fragment recipe_list = new FragmentRecipeList();
    Fragment recipe_detail = new FragmentRecipeDetail();
    FragmentGrocery fragmentGrocery = new FragmentGrocery();
    public FragmentManager fragmentManager;
    FragmentTransaction transaction;

    NetworkService networkService;

    Boolean mode = false;


    //네트워크 통신 변수 선언
    ArrayList<Dvsensor> dvsensorTemp;
    ArrayList<Dvsensor> dvsensorHumi;

    ArrayList<Follow> myfollowList;

    User user;
    Recipe recommendRecipe;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("프래그먼트 탐구", "여기도 들어오나요?");
        if (getArguments() != null) {
            Log.e("프래그먼트 탐구", "여기도 들어오나요?222");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




        transaction = getFragmentManager().beginTransaction();

        //네트워크 (레트로핏 불러오기)
        networkService = ApplicationController.getInstance().getNetworkService();






    }

    @Override
    public void onResume() {
        super.onResume();

        networkService = ApplicationController.getInstance().getNetworkService();
        transaction = getFragmentManager().beginTransaction();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.story_listview);
        food_detail_btn = view.findViewById(R.id.food_detail_btn);
        food_list_btn = view.findViewById(R.id.food_list_btn);

        food_thumb_iv = view.findViewById(R.id.food_thumb_iv);
        mode_change_iv = view.findViewById(R.id.mode_change_iv);

        temp_tv = view.findViewById(R.id.temp_tv);
        humi_tv = view.findViewById(R.id.humi_tv);
        outing_state_tv = view.findViewById(R.id.outing_state_tv);

        main_recipe_ingredient_tv = view.findViewById(R.id.main_recipe_ingredient_tv);
        main_recipe_food_tv = view.findViewById(R.id.main_recipe_food_tv);

        follow_no_noti = view.findViewById(R.id.follow_no_noti);

        follow_no_noti.setVisibility(View.GONE);

        alpha = ((LinearLayout) view.findViewById(R.id.goodhomelv)).getBackground();
        title = view.findViewById(R.id.recipe_list_title_tv);

        alpha.setAlpha(60);
        GradientDrawable drawable = (GradientDrawable) getContext().getDrawable(R.drawable.image_background);
        food_thumb_iv.setBackground(drawable);
        food_thumb_iv.setClipToOutline(true);

        //서버통신(문제의 부분 본래 레트로핏 비동기로 함수화해서 네개(getTemp,getHumi, get- ,get 등)를 호출하고 안되어 동기적 방식으로 돌려도 되지않음.(로딩8초)

        new AsyncTask<Void, Void, Void>(){

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(activity, "로딩중", "잠시만 기다려주세요.");


            }

            @Override
            protected Void doInBackground(Void... voids) {

                Call<ArrayList<Dvsensor>> callSensorTemp = networkService.getSensorValue(ApplicationController.user_id,"temp");
                Call<ArrayList<Dvsensor>> callSensorHumi = networkService.getSensorValue(ApplicationController.user_id,"humi");
                Call<Recipe> getMainRecommendRecipe = networkService.getMainRecommendRecipe(ApplicationController.user_id);

                Call<ArrayList<Follow>> getFollowList = networkService.getFollowList(ApplicationController.user_id);

                Call<User> getUserInfo = networkService.getUserInfo(ApplicationController.user_id);

                try {
                    dvsensorTemp = new ArrayList<>();
                    dvsensorTemp = callSensorTemp.execute().body();

                    dvsensorHumi = new ArrayList<>();
                    dvsensorHumi = callSensorHumi.execute().body();

                    recommendRecipe = new Recipe();
                    recommendRecipe = getMainRecommendRecipe.execute().body();

                    myfollowList = new ArrayList<Follow>();
                    myfollowList = getFollowList.execute().body();

                    user = new User();
                    user = getUserInfo.execute().body();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                temp_tv.setText(String.valueOf(dvsensorTemp.get(0).getValue()));
                humi_tv.setText(String.valueOf(dvsensorHumi.get(0).getValue()));

                main_recipe_food_tv.setText(recommendRecipe.getName());
                main_recipe_ingredient_tv.setText(recommendRecipe.getIngredient());
                Glide.with(getContext())
                        .load(recommendRecipe.getImg())
                        .into(food_thumb_iv);

                if(myfollowList.size()==0){
                    follow_no_noti.setVisibility(View.VISIBLE);
                }else{
                    layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new StoryAdapter(getContext(), myfollowList);
                    recyclerView.setAdapter(adapter);
                }

                if(user.outing_mode ==0){
                    outing_state_tv.setText("OFF");
                    outing_state_tv.setTextColor(BLACK);
                    mode =false;
                }else if(user.outing_mode ==1){
                    outing_state_tv.setText("ON");
                    outing_state_tv.setTextColor(RED);
                    mode = true;
                }

                progressDialog.dismiss();

                activity.okChange();

            }

        }.execute();



//        getHumi();
//        getFollowList();
//        getRecommendRecipe();
//







        //즐겨찾기 레시피 조회
        activity.recipe_likelist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
//                bundle.putString("bundelName","list");
                bundle.putString("listName", "bookmark");
                recipe_list.setArguments(bundle);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, recipe_list);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.e("눌렸나요?버튼", "넹!!!!!1");
            }
        });
        //레시피 목록 조회 버튼
        activity.recipe_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
//                bundle.putString("bundelName","list");
                bundle.putString("listName", "recommend");
                recipe_list.setArguments(bundle);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, recipe_list);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.e("눌렸나요?버튼", "넹!!!!!2");
            }
        });

        food_list_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
//                bundle.putString("bundelName","list");
                bundle.putString("listName", "recommend");
                recipe_list.setArguments(bundle);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, recipe_list);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        food_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("RecipeValue", recommendRecipe);
                recipe_detail.setArguments(bundle);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, recipe_detail);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        mode_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ;

                    if (mode == false) {
                        changeOuting(1);


                    } else if (mode == true) {
                        changeOuting(0);

                    }



            }
        });





        // Inflate the layout for this fragment
        return view;
    }


    public void getTemp(){
        Call<ArrayList<Dvsensor>> callSensorTemp = networkService.getSensorValue(ApplicationController.user_id,"temp");
        callSensorTemp.enqueue(new Callback<ArrayList<Dvsensor>>() {
            @Override
            public void onResponse(Call<ArrayList<Dvsensor>> call, Response<ArrayList<Dvsensor>> response) {
                dvsensorTemp = new ArrayList<>();
                dvsensorTemp = response.body();
                temp_tv.setText(String.valueOf(dvsensorTemp.get(0).getValue()));

                Log.e("제발 제발 들어와주세요 부탁입니다",response.message());
                Log.e("제발 제발 들어와주세요 부탁입니다",String.valueOf(dvsensorTemp.size()));

            }

            @Override
            public void onFailure(Call<ArrayList<Dvsensor>> call, Throwable t) {
                Log.e("무슨 문제인지 너는 아니?",t.getMessage());

            }
        });
    }

    public void getHumi(){
        Call<ArrayList<Dvsensor>> callSensorHumi = networkService.getSensorValue(ApplicationController.user_id,"humi");
        callSensorHumi.enqueue(new Callback<ArrayList<Dvsensor>>() {
            @Override
            public void onResponse(Call<ArrayList<Dvsensor>> call, Response<ArrayList<Dvsensor>> response) {
                dvsensorHumi = new ArrayList<>();
                dvsensorHumi = response.body();
                humi_tv.setText(String.valueOf(dvsensorHumi.get(0).getValue()));

                Log.e("제발 제발 들어와주세요 부탁입니다 휴미",response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<Dvsensor>> call, Throwable t) {

            }
        });
    }

    public void getRecommendRecipe(){
        Call<Recipe> getMainRecommendRecipe = networkService.getMainRecommendRecipe(ApplicationController.user_id);
        getMainRecommendRecipe.enqueue((new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Log.e("제발 제발 들어와주세요 부탁입니다 레시피",response.message());
                recommendRecipe = new Recipe();
                recommendRecipe = response.body();


                main_recipe_food_tv.setText(recommendRecipe.getName());
                main_recipe_ingredient_tv.setText(recommendRecipe.getIngredient());
                Glide.with(getContext())
                        .load(recommendRecipe.getImg())
                        .into(food_thumb_iv);

                Log.e("제발 제발 들어와주세요 부탁입니다 레시피",response.message());


            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {

            }
        }));
    }

    public void getFollowList(){
        Call<ArrayList<Follow>> getFollowList = networkService.getFollowList(ApplicationController.user_id);
        getFollowList.enqueue(new Callback<ArrayList<Follow>>() {
            @Override
            public void onResponse(Call<ArrayList<Follow>> call, Response<ArrayList<Follow>> response) {
                myfollowList = new ArrayList<Follow>();
                myfollowList = response.body();

                if(myfollowList.size()==0){
                    follow_no_noti.setVisibility(View.VISIBLE);
                }else{
                    layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new StoryAdapter(getContext(), myfollowList);
                    recyclerView.setAdapter(adapter);
                }




                Log.e("제발 제발 들어와주세요 부탁입니다 팔로우",response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<Follow>> call, Throwable t) {

            }
        });
    }
    public void changeOuting(int i){
        Call<Void> changeOuting = networkService.changeOuting(new Mode(ApplicationController.user_id,i));
        changeOuting.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.e("외출모드", mode.toString());
                Log.e("외출모드 코드", String.valueOf(response.code()));
                if(mode ==false){
                    mode_change_iv.setImageResource(R.drawable.exit3);
                    outing_state_tv.setText("ON");
                    outing_state_tv.setTextColor(RED);
                    mode = true;
                }else if(mode==true){
                    mode_change_iv.setImageResource(R.drawable.homein);
                    outing_state_tv.setText("OFF");
                    outing_state_tv.setTextColor(BLACK);
                    mode = false;

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }





}




