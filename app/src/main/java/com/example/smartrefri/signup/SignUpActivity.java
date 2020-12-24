package com.example.smartrefri.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartrefri.CustomDialog;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.AlarmAddActivity;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.User;
import com.example.smartrefri.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    EditText email_et,password_et,name_et,age_et,phone_et,guardian_name_et,guardian_phone_et;
    RadioGroup sex_radio;
    RadioButton female_radio_btn,male_radio_btn;
    Spinner purpose_drop;
    ImageView signup_complete_btn;

    NetworkService networkService;

    //변수 선언
    public String email;
    public int age;
    public int sex;
    public String phone_number;
    public String name;
    public String password;

    public String guardian_name;
    public String guardian_phone_number;

    public String purpose;

    public User user;
    
    //다이얼로그
    CustomDialog insertCheckDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        name_et = findViewById(R.id.name_et);
        age_et = findViewById(R.id.age_et);
        phone_et = findViewById(R.id.phone_et);
        guardian_name_et = findViewById(R.id.guardian_name_et);
        guardian_phone_et = findViewById(R.id.guardian_phone_et);

        sex_radio = findViewById(R.id.sex_radio);
        female_radio_btn = findViewById(R.id.female_radio_btn);
        male_radio_btn = findViewById(R.id.male_radio_btn);
        purpose_drop = findViewById(R.id.purpose_drop);

        phone_et.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        guardian_phone_et.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        signup_complete_btn = findViewById(R.id.signup_complete_btn);

        insertCheckDialog = new CustomDialog(SignUpActivity.this, CancelListener, OkListener);

        sex=0;




        networkService = ApplicationController.getInstance().getNetworkService();

        signup_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = email_et.getText().toString();
                age = Integer.valueOf(age_et.getText().toString());
                phone_number =  phone_et.getText().toString();
                name = name_et.getText().toString();
                password = password_et.getText().toString();
                guardian_name = guardian_name_et.getText().toString();
                guardian_phone_number = guardian_phone_et.getText().toString();
                purpose = purpose_drop.getSelectedItem().toString();

                Log.e("purpose값",purpose);

                user = new User(email, age,sex,phone_number, name, password,guardian_name,guardian_phone_number,purpose);

                addAlarmDialog();

            }
        });

        sex_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.female_radio_btn){
                    sex = 2;
                }else if(i == R.id.male_radio_btn){
                    sex = 1;
                }
            }
        });



    }


    public void signUp(User user){
        Call<Void> signUpUser = networkService.signUpUser(user);
        signUpUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("회원가입 성공했나요?",response.message());
                Log.e("회원가입 코드",String.valueOf(response.code()));

                if(response.isSuccessful()){

                    Intent intent = new Intent(getApplicationContext(),InsertFridgeActivity.class);
                    intent.putExtra("email",user);
                    startActivity(intent);
                    finish();


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    public void addAlarmDialog() {
        insertCheckDialog.show();
        insertCheckDialog.setTitle("회원가입");
        insertCheckDialog.setContent("입력하신 정보로 회원가입하시겠습니까?");

    }

    private View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            insertCheckDialog.dismiss();

        }

    };
    private View.OnClickListener OkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (email_et.length() ==0 || password_et.length() == 0 || name_et.length()==0 ||sex == 0 || phone_et.length()==0 || guardian_name_et.length()==0|| guardian_phone_et.length()==0) {
                Toast.makeText(getApplicationContext(), "빈 항목이 있는지 확인해주세요!", Toast.LENGTH_SHORT).show();
            }else{


                insertCheckDialog.dismiss();
                signUp(user);

            }
        }
    };
}