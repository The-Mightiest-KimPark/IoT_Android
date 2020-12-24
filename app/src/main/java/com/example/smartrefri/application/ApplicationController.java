package com.example.smartrefri.application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.example.smartrefri.model.Alarm;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.network.NetworkService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.util.ArrayList;

public class ApplicationController extends Application {
    private static volatile ApplicationController instance = null;
    private static volatile Activity currentActivity = null;

    public static ArrayList<Alarm> groceryAlarm;
    public static ArrayList<Grocery> grocery;

    public static String user_id ="";



    //서버 유알엘
    private static String baseUrl = "http://3.92.44.79:80";

    private static NetworkService networkService ;


    public static void setGroceryAlarm(ArrayList<Alarm> groceryAlarm) {
        ApplicationController.groceryAlarm = groceryAlarm;
    }
    public static ArrayList<Alarm> getGroceryAlarm(){
        return ApplicationController.groceryAlarm;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }



    public static ApplicationController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // TODO: 2016. 11. 21. 어플리케이션 초기 실행 시, retrofit을 사전에 build한다.
        this.buildService();



    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        ApplicationController.currentActivity = currentActivity;
    }
    public static ApplicationController getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
    public void buildService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)) // json 사용할꺼기때문에 json컨버터를 추가적으로 넣어준것임
                .build();
        networkService = retrofit.create(NetworkService.class);
    }

}
