package com.example.smartrefri.mypage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.model.Alarm;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager nm;
    ArrayList<Alarm> groceryAlarm;

    public void onReceive(Context context, Intent intent) {
            Log.e("리시브 들어왔나요?","진짜왜안되는건데");


            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.refri);

            builder.setTicker("근처에 즐겨찾기한 가게가 있어요!");
            builder.setWhen(System.currentTimeMillis());//알람시간에 notification표시
            builder.setNumber(10);//notification을 클릭하면 알람 앱 화면 실행
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            builder.setVibrate(new long[]{1000, 1000, 500, 500, 200, 200, 200, 200, 200, 200});
            builder.setLights(Color.BLUE, 500, 500);
            Notification noti = builder.build();


            noti.flags = noti.flags | noti.FLAG_AUTO_CANCEL;
            nm.notify(1, noti);
        }
    }




