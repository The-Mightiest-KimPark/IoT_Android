package com.example.smartrefri.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import androidx.appcompat.app.ActionBar;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.smartrefri.CustomDialog;
import com.example.smartrefri.R;
import com.example.smartrefri.Recipe.FragmentRecipeDetail;
import com.example.smartrefri.Recipe.FragmentRecipeList;
import com.example.smartrefri.Refri.FragmentRefri;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.FragmentGrocery;
import com.example.smartrefri.model.Dvsensor;
import com.example.smartrefri.model.Recipe;
import com.example.smartrefri.mypage.FragmentMyPage;
import com.example.smartrefri.network.NetworkService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //프래그먼트 이용하려면 필요한 변수 설정
    public FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentMain fragmentMain = new FragmentMain();
    FragmentMyPage fragmentMyPage = new FragmentMyPage();
    FragmentRefri fragmentRefri = new FragmentRefri();
    FragmentGrocery fragmentGrocery = new FragmentGrocery();

    public FragmentTransaction transaction;
    Toolbar toolbar;
    ActionBar actionBar;

    Intent intent;

    CustomDialog exitDialog;

    ImageView recipe_list_btn,recipe_likelist_btn;

    NetworkService networkService;

    FragmentRecipeDetail fragmentRecipeDetail = new FragmentRecipeDetail();
    FragmentRecipeList fragmentRecipeList = new FragmentRecipeList();

    public static boolean changeF;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkService = ApplicationController.getInstance().getNetworkService();


        intent = getIntent();

        changeF = false;





        recipe_list_btn = findViewById(R.id.recipe_list_btn);
        recipe_likelist_btn = findViewById(R.id.recipe_likelist_btn);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);

        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(false);//뒤로가기버튼




        transaction = fragmentManager.beginTransaction();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


        bottomNavigationView.setSelectedItemId(R.id.homeItem);
//        transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
//
//        Log.e("인텐트 값 있니?", String.valueOf(getIntent()));
//        Log.e("인텐트 널값 뭐라고 뜨나요?", String.valueOf(TextUtils.isEmpty(intent.getStringExtra("intentName"))));
//        if(TextUtils.isEmpty(intent.getStringExtra("intentName"))) {
//            Log.e("설마 여기로들어오는건","아니겠찌..");
//            bottomNavigationView.setSelectedItemId(R.id.homeItem);
//            transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
//        }else {
//            if (intent.getStringExtra("intentName").equals("refri")) {
//                Log.e("어디로왔니?","냉장고요");
//                bottomNavigationView.setSelectedItemId(R.id.RefriItem);
//                transaction.replace(R.id.frameLayout, fragmentRefri).commitAllowingStateLoss();
//            } else if (intent.getStringExtra("intentName").equals("home")) {
//                Log.e("어디로왔니?","홈이요");
//                bottomNavigationView.setSelectedItemId(R.id.homeItem);
//                transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
//            } else if (intent.getStringExtra("intentName").equals("mypage")) {
//                Log.e("어디로왔니?","마이페이지요");
//                bottomNavigationView.setSelectedItemId(R.id.mypageItem);
//                transaction.replace(R.id.frameLayout, fragmentMyPage).commitAllowingStateLoss();
//            }
//        }

    }





//        void changeFragment(String fragment){
//        transaction = fragmentManager.beginTransaction();
//        switch (fragment){
//            case "recipe_detail":
//                Log.e("프래그먼트 이동","되나요?1");
//                transaction.replace(R.id.frameLayout, fragmentRecipeDetail).commitAllowingStateLoss();
//                break;
//            case "recipe_list":
//                Log.e("프래그먼트 이동","되나요?2");
//                transaction.replace(R.id.frameLayout, fragmentRecipeList).commitAllowingStateLoss();
//                break;
//        }
//    }
    public void changeRecipeBtn(){
        recipe_likelist_btn.setVisibility(View.VISIBLE);

        recipe_list_btn.setVisibility(View.VISIBLE);

    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            transaction = fragmentManager.beginTransaction();
            recipe_likelist_btn.setVisibility(View.GONE);
            recipe_list_btn.setVisibility(View.GONE);
            switch(menuItem.getItemId())
            {
                case R.id.RefriItem:
                    transaction.replace(R.id.frameLayout, fragmentRefri).commitAllowingStateLoss();

                    break;
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
                    break;
                case R.id.mypageItem:
                    transaction.replace(R.id.frameLayout, fragmentMyPage).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    public void okChange(){

        changeF = true;
        if(intent.getStringExtra("datenoti")==null){

        }else{
            if(intent.getStringExtra("datenoti").equals("datenoti")){
                Log.e("여기로 오긴하나요?","네..");
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout, fragmentGrocery);
                transaction.commit();
            }
        }
    }
    private View.OnClickListener BackCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitDialog.dismiss();
        }
    };
    private View.OnClickListener BackOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            super.onBackPressed();
        }else {
            exitDialog = new CustomDialog(MainActivity.this, BackCancelListener, BackOkListener);
            exitDialog.show();
            exitDialog.setTitle("종료");
            exitDialog.setContent("종료하시겠습니까?");
        }

    }


}