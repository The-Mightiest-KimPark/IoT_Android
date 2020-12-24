package com.example.smartrefri.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartrefri.R;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryPicActivity extends AppCompatActivity {

    Handler handler;
    Intent intent;
    TextView username_tv,date_tv;
    CircleImageView user_iv;
    ImageView story_pic_iv;

    ProgressBar progressBar;
    int count = 0;

    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story_pic);

        Intent intent = getIntent();
        user_iv = findViewById(R.id.story_circle);
        username_tv = findViewById(R.id.username_tv);
        story_pic_iv = findViewById(R.id.story_pic_iv);
        date_tv = findViewById(R.id.date_tv);
        progressBar = findViewById(R.id.story_pro);



        //회원이미지
        if(intent.getStringExtra("userImage")==null){

            if(intent.getIntExtra("sex",0)==1){
                Glide.with(getApplicationContext())
                        .load(R.drawable.male)
                        .into(user_iv);
            }else if(intent.getIntExtra("sex",0)==2){
                Glide.with(getApplicationContext())
                        .load(R.drawable.female)
                        .into(user_iv);
            }
        }else{
            Glide.with(getApplicationContext())
                    .load(intent.getStringExtra("userImage"))
                    .into(user_iv);
        }

        //냉장고이미지
        Glide.with(getApplicationContext())
                .load(intent.getStringExtra("refriImage"))
                .into(story_pic_iv);
        username_tv.setText(intent.getStringExtra("username"));
        date_tv.setText(intent.getStringExtra("reg_date"));


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                finish();

            }
        };

        handler.sendEmptyMessageDelayed(0, 10000);
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress(count);
                count++;
            }
        };
        Log.e("스토리 초기화","되잖아너");
        progressBar.setMax(100);
        progressBar.setProgress(0);
        timer = new Timer();
        timer.schedule(tt,0,100);





    }


    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        timer = null;
        Thread.interrupted();
        finish();
        Log.e("타이머 초기화 안되나요?","왜죠?2");
    }
}