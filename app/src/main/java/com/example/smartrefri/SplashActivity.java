package com.example.smartrefri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.User;
import com.example.smartrefri.network.NetworkService;
import com.example.smartrefri.signin.SignInActivity;
import com.example.smartrefri.signup.InsertFridgeActivity;

import retrofit2.Call;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Intent intent;

    SharedPreferences loginInformation;
    SharedPreferences.Editor editor;

    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        loginInformation = getSharedPreferences("setting",MODE_PRIVATE);

        networkService = ApplicationController.getInstance().getNetworkService();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(loginInformation.getString("id","")==null || loginInformation.getString("id","").equals("")) {

                        Log.e("자동로그인아닐때,로그아웃했을때","여기얌");

                        intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();

                }
                else {

                    Log.e("자동로그인이얌","여기얌");

                    ApplicationController.user_id = loginInformation.getString("id","");


                    checkFridgeNumber(ApplicationController.user_id);



                }


            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        //여기서 자동로그인체크해줘야함
        handler.sendEmptyMessageDelayed(0, 2000);
    }


    public void checkFridgeNumber(String email){
        Call<User> checkFridge = networkService.checkFridge(email);
        checkFridge.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = new User(response.body());

                if(user.result == false){
                    Log.e("아이디뭔가요?",ApplicationController.user_id);
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(user.result == true){
                    Log.e("아이디뭔가요?",ApplicationController.user_id);
                    intent = new Intent(getApplicationContext(), InsertFridgeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("어디로들어온거니",t.getMessage());
            }
        });
    }


    }
