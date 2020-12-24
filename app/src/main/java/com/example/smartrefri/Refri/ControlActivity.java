package com.example.smartrefri.Refri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartrefri.R;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ControlActivity extends AppCompatActivity {

    ImageView refri_door_control_btn,refri_temp_season_iv;
    LinearLayout refri_temp_control_btn;
    TextView refri_temp_season_tv;



    //파이 주소
    public static final String MQTT_BROKER_IP ="tcp://192.168.0.122:1883";
//    우리집
//    public static final String MQTT_BROKER_IP = "tcp://172.30.1.2:1883";
    public static MqttClient pubClient;

    int door_state;
    //0:여름 1: 겨울
    int season_state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        //뷰설정
        refri_door_control_btn = findViewById(R.id.refri_door_control_btn);
        refri_temp_season_iv = findViewById(R.id.refri_temp_season_iv);
        refri_temp_control_btn = findViewById(R.id.refri_temp_control_btn);
        refri_temp_season_tv = findViewById(R.id.refri_temp_season_tv);

        door_state = 0;
        season_state = 1;

        //mqtt설정
        if(pubClient == null)
            TransMqttThread.start();
        
        if(door_state==1){
            refri_door_control_btn.setImageResource(R.drawable.switchon);
        }else{
            refri_door_control_btn.setImageResource(R.drawable.switchoff);
        }
        
        if(season_state==0){
            refri_temp_season_iv.setImageResource(R.drawable.sun);
            refri_temp_season_tv.setText("하절기");
        }else{
            refri_temp_season_iv.setImageResource(R.drawable.snowman);
            refri_temp_season_tv.setText("동절기");
        }
        
        



        //네트워크받아서 문상태가 0면 열기 1이면 닫기
        refri_door_control_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pubClient == null)
                    TransMqttThread.start();
                if(door_state==1){
                    publishMessage("sensors/door/android/close","1");
//                    임시값변환(실제로는 이때 서버에서 도어스테이트가 진짜로 0이면 0을 넣어줌
                    door_state=0;
                    refri_door_control_btn.setImageResource(R.drawable.switchoff);
                }else if(door_state==0){
                    publishMessage("sensors/door/android/open","1");
                    door_state=1;
                    refri_door_control_btn.setImageResource(R.drawable.switchon);
                }

            }
        });

        refri_temp_control_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pubClient == null)
                    TransMqttThread.start();
                if(season_state ==0){
                    //여름일때
                    refri_temp_season_iv.setImageResource(R.drawable.snowman);
                    refri_temp_season_tv.setText("동절기");
                    season_state=1;
                }else if(season_state==1){
                    //겨울일때
                    refri_temp_season_iv.setImageResource(R.drawable.sun);
                    refri_temp_season_tv.setText("하절기");
                    season_state=0;
                }
                
            }
        });

    }

    public static Thread TransMqttThread = new Thread(){
        @Override
        public void run() {
            try{
                pubClient = new MqttClient(MQTT_BROKER_IP,MqttClient.generateClientId(),new MemoryPersistence());
                pubClient.connect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public static void publishMessage(String TOPIC, String buttonMsg){
        try{
            final MqttTopic topic = pubClient.getTopic(TOPIC);

            final String msg = buttonMsg;
            final MqttMessage pubMsg = new MqttMessage(msg.getBytes());

            topic.publish(pubMsg);

            System.out.println("Publishe data. Topic: " + topic.getName() + "Message: "+msg);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

}