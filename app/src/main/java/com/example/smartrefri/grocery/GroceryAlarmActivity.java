package com.example.smartrefri.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.model.Alarm;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceryAlarmActivity extends AppCompatActivity {

    RecyclerView grocery_alarm_rv;
    LinearLayoutManager layoutManager;
    GroceryAlarmAdapter groceryAlarmAdapter;
    ArrayList<Alarm> alarmGroceryList;
    ImageView alarm_add_complete_btn;
    NetworkService networkService;

    Intent intent;

    GroceryAlarmActivity groceryAlarmActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_alarm);
        grocery_alarm_rv = findViewById(R.id.grocery_alarm_rv);
        alarm_add_complete_btn = findViewById(R.id.alarm_add_complete_btn);

        networkService = ApplicationController.getInstance().getNetworkService();

        groceryAlarmActivity = this;

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grocery_alarm_rv.setLayoutManager(layoutManager);






        alarm_add_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AlarmAddActivity.class);
                startActivity(intent);

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        getAlarmList();

    }


    public void getAlarmList(){
        Call<ArrayList<Alarm>> callGroceryAlarm = networkService.getGroceryAlarmList(ApplicationController.user_id);
        callGroceryAlarm.enqueue(new Callback<ArrayList<Alarm>>() {
            @Override
            public void onResponse(Call<ArrayList<Alarm>> call, Response<ArrayList<Alarm>> response) {
                ArrayList<Alarm> alarmArrayList = new ArrayList<Alarm>();
                alarmArrayList = response.body();
                groceryAlarmAdapter = new GroceryAlarmAdapter(alarmArrayList,getApplicationContext(),groceryAlarmActivity);
                grocery_alarm_rv.setAdapter(groceryAlarmAdapter);


            }

            @Override
            public void onFailure(Call<ArrayList<Alarm>> call, Throwable t) {

            }
        });

    }
}