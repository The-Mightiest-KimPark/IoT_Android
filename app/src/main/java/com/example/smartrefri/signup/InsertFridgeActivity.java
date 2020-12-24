package com.example.smartrefri.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.Fridge;
import com.example.smartrefri.network.NetworkService;

import retrofit2.Call;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class InsertFridgeActivity extends AppCompatActivity {

    EditText fridge_number_et;
    TextView fridge_number_btn;

    NetworkService networkService;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_fridge);

        fridge_number_et = findViewById(R.id.fridge_number_et);
        fridge_number_btn = findViewById(R.id.fridge_number_btn);

        networkService = ApplicationController.getInstance().getNetworkService();


        fridge_number_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fridge fridge = new Fridge(fridge_number_et.getText().toString(),intent.getStringExtra("email"));
                insertFridge(fridge);

            }
        });


    }

    public void insertFridge(Fridge fridge){
        Call<Void> putFridge = networkService.putFridge(fridge);
        putFridge.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.e("냉장고 인증 코드",String.valueOf(response.code()));
                Log.e("냉장고 인증 메세지",response.message());

                if(response.isSuccessful()){
                    ApplicationController.user_id = intent.getStringExtra("email");
                    Intent intent = new Intent(InsertFridgeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }else{
                    Toast.makeText(InsertFridgeActivity.this,"등록되지않은 냉장고 정보입니다",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}