package com.example.smartrefri.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.User;
import com.example.smartrefri.network.NetworkService;
import com.example.smartrefri.signup.InsertFridgeActivity;
import com.example.smartrefri.signup.SignUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity {

    EditText signin_email_et, signin_password_et;
    CheckBox signin_auto_checkbox;

    TextView signin_to_signup_btn;

    Button signin_btn;

    NetworkService networkService;

    User user;

    SharedPreferences loginInformation;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signin_email_et = findViewById(R.id.signin_email_et);

        signin_password_et = findViewById(R.id.signin_password_et);

        signin_auto_checkbox = findViewById(R.id.signin_auto_checkbox);

        signin_to_signup_btn = findViewById(R.id.signin_to_signup_btn);

        signin_btn = findViewById(R.id.signin_btn);

        networkService = ApplicationController.getInstance().getNetworkService();



        signin_to_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);



            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = new User(signin_email_et.getText().toString(),signin_password_et.getText().toString() );
                Log.e("로그인 하기전에 id",signin_email_et.getText().toString());
                Log.e("로그인 하기전에 비밀번호",signin_password_et.getText().toString());
                signinUser(user);

            }
        });





    }
    public void signinUser(User user){
        Call<Void> signInUser = networkService.signInUser(user);
        signInUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("로그인 성공했나요?",response.message());
                Log.e("로그인 코드",String.valueOf(response.code()));

                if(response.isSuccessful()){
                    if(signin_auto_checkbox.isChecked()){
                        //자동로그인 SharedPreference에 저장
                        loginInformation = getSharedPreferences("setting",MODE_PRIVATE);
                        editor = loginInformation.edit();
                        editor.putString("id",user.getEmail());
                        editor.commit();
                        ApplicationController.user_id = user.getEmail();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("email",user.getEmail());
                            startActivity(intent);
                            finish();


                    }else{

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        Application Controller에 user_id에 저장
                            ApplicationController.user_id = user.getEmail();
                        Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다",Toast.LENGTH_SHORT);
                            startActivity(intent);
                            finish();



                    }

                    


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Log.e("로그인실패원인",t.getMessage());
            }
        });
    }
}