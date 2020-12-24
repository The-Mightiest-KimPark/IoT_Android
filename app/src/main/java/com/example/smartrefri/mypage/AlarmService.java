package com.example.smartrefri.mypage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.GroceryAdapter;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.Alarm;
import com.example.smartrefri.model.Dvsensor;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.network.NetworkService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    NetworkService networkService;

    ArrayList<Alarm> groceryAlarm;
    ArrayList<Grocery> groceryList;

    Intent intent;

    Handler handler;
    requestAsyncTask asyncTask;

    boolean state;

    NotificationManager nm;
    Notification.Builder builder;


    PendingIntent proximityIntent;
    String alarmMsg;

    boolean exist;

    boolean exist2;

    ArrayList<Grocery> customGroceryList;

    Date FirstDate;
    Date SecondDate;
    Long calDateDays;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;


        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.e("서비스:", "최초실행");
        networkService = ApplicationController.getInstance().getNetworkService();
        state=true;
        Timer timer = new Timer(true); //인자가 Daemon 설정인데 true 여야 죽지 않음.
        Handler handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    public void run(){

                        if(state==true) {
                            asyncTask = new requestAsyncTask();
                            asyncTask.execute();
                        }
                    }
                });
            }
        }, 0, 10000);
    }

    @Override
    public void onDestroy() {
        state =false;
        try
        {
            if (asyncTask.getStatus() == AsyncTask.Status.RUNNING)
            {
               asyncTask.cancel(true);
            }
            else
            {
            }
        }
        catch (Exception e)
        {
        }

        Log.e("서비스:", "종료");
        super.onDestroy();
        // 서비스가 종료될 때 실행

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.e("test", "서비스의 onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    public class requestAsyncTask extends AsyncTask<String,Void,String>{

        public String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }



        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... strings) {

                groceryAlarm = new ArrayList<Alarm>();
                groceryList = new ArrayList<Grocery>();


                Call<ArrayList<Alarm>> callGroceryAlarm = networkService.getGroceryAlarmList(ApplicationController.user_id);
                callGroceryAlarm.enqueue(new Callback<ArrayList<Alarm>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Alarm>> call, Response<ArrayList<Alarm>> response) {

                        Log.e("서비스", "알람목록받아옴");

                        groceryAlarm = response.body();

                        Log.e("몇개 들어왔습니까?1",String.valueOf(groceryAlarm.size()));
                        ns();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Alarm>> call, Throwable t) {
                        Log.e("실패원인", t.getMessage());
                    }
                });

                getFlame();
            getCustomGroceryList();



                return result;

        }
    }

    public void ns(){
        //현재 나의 식재료

        Call<ArrayList<Grocery>> callGroceryList = networkService.getGroceryListALL(ApplicationController.user_id);
        callGroceryList.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                Log.e("서비스", "식재료목록받아옴");
                groceryList = response.body();
                Log.e("몇개 들어왔습니까?2",String.valueOf(groceryList.size()));

                checkAlarmCount();

            }


            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

                Log.e("실패원인", t.getMessage());
            }
        });
    }

    public void checkAlarmCount(){
        Log.e("서비스 여기 들어오긴했나요?", "드어왔냐고요");

        ArrayList<Alarm> castAlarmList = new ArrayList<Alarm>();
        ArrayList<Alarm> vsAlarmList = new ArrayList<>();

        int allCount=0;

        for(int i=0; i<groceryAlarm.size(); i++){
            exist=false;
            for(int j=0; j<groceryList.size(); j++) {
                Log.e("식재료는 제대로된거맞아?",groceryList.get(j).getName());
                Log.e("입력갯수 제대로 된거맞아?",String.valueOf(groceryList.get(j).getCount()));


                if (groceryAlarm.get(i).getAll_grocery_id() == groceryList.get(j).getGrocery_id()) {
                    Log.e("입력갯수 제대로 된거맞아?",String.valueOf(groceryList.get(i).getCount()));
                    castAlarmList.add(new Alarm(groceryAlarm.get(i).getAll_grocery_id(), groceryList.get(j).getCount(), groceryAlarm.get(i).getName()));
                    exist = true;
                }


            }
            if(exist==false){

                castAlarmList.add(new Alarm(groceryAlarm.get(i).getAll_grocery_id(), 0, groceryAlarm.get(i).getName()));
            }

        }

        Log.e("변환한거 갯수는?",String.valueOf(castAlarmList.size()));
        for(int i=0;i<castAlarmList.size();i++){

            Log.e("변환한거 다 읊어봐",castAlarmList.get(i).getName());
            Log.e("변환한거 다 읊어봐 식재료 갯수",String.valueOf(castAlarmList.get(i).getCount()));
        }

        for(int x=0; x<groceryAlarm.size();x++){

            for(int y=0; y < castAlarmList.size();y++){
                if(groceryAlarm.get(x).getAll_grocery_id() ==  castAlarmList.get(y).getAll_grocery_id()){

                    if(vsAlarmList.size()==0){
                        Log.e("x가 0일때 한번 들어와야함",String.valueOf(x));
                        vsAlarmList.add(new Alarm(groceryAlarm.get(x).getAll_grocery_id(),castAlarmList.get(y).getCount(),groceryAlarm.get(x).getName()));
                    }else{


                        Log.e("왜또들어가는건지 알수가없네 ",String.valueOf(vsAlarmList.size()));
                        int count = vsAlarmList.size();
                            for(int a=0; a<count;a++){
                                if(vsAlarmList.get(a).getAll_grocery_id() == groceryAlarm.get(x).all_grocery_id){
                                    Log.e("여기도 들어와야죠?",String.valueOf(x));
                                    allCount = vsAlarmList.get(a).getCount() + castAlarmList.get(y).getCount();

                                    vsAlarmList.set(a,new Alarm(groceryAlarm.get(x).getAll_grocery_id(),allCount,groceryAlarm.get(x).getName()));
                                }else{
                                    Log.e("여기까지 와야해",groceryAlarm.get(x).getName());
                                    Log.e("여기까지 와야해",String.valueOf(castAlarmList.get(y).getCount()));
                                    vsAlarmList.add(new Alarm(groceryAlarm.get(x).getAll_grocery_id(),castAlarmList.get(y).getCount(),groceryAlarm.get(x).getName()));

                                }
                            }




                    }
                }
            }
        }
        Log.e("이름까지 변환한거 갯수",String.valueOf(vsAlarmList.size()));
        Log.e("vs알람리스트",vsAlarmList.get(0).getName());
        Log.e("vs알람리스트",String.valueOf(vsAlarmList.get(0).getCount()));

        Log.e("vs알람리스트",vsAlarmList.get(1).getName());
        Log.e("vs알람리스트",String.valueOf(vsAlarmList.get(1).getCount()));
        ArrayList<Alarm> lastAlarm = new ArrayList<Alarm>();
        for(int g=0;g<groceryAlarm.size();g++){
            if(vsAlarmList.get(g).getCount() <= groceryAlarm.get(g).getCount()){
                Log.e("vs알람리스트",vsAlarmList.get(g).getName());
                Log.e("vs알람리스트",String.valueOf(vsAlarmList.get(g).getCount()));
                lastAlarm.add(vsAlarmList.get(g));
                noti(lastAlarm);
            }
        }
    }

    public void noti(ArrayList<Alarm> alarms){
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        }

        int count = alarms.size();
        Log.e("알람넣기전에 알람리스트 확인해보자",String.valueOf(count));
        alarmMsg="";
        for(int i=0; i<alarms.size();i++){

            if(i==0){
                alarmMsg = alarms.get(i).getName()+"가 "+ alarms.get(i).getCount() +"개 남았습니다\n";
            }
            else{
                alarmMsg+=alarms.get(i).getName()+"가 "+ alarms.get(i).getCount() +"개 남았습니다\n";
            }

        }
        Bitmap mLargeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.hungry);
        Log.e("알람넣기전에 알람리스트 확인해보자메세지",alarmMsg);
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.title)
                .setContentTitle("식재료가 부족해요!")
                .setLargeIcon(mLargeIcon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(alarmMsg))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000, 500, 500, 200, 200, 200, 200, 200, 200})
                .setLights(Color.BLUE, 500, 500);
        notificationManager.notify(2, notificationBuilder.build());


        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "My:Tag");
        wakeLock.acquire(5000);
    }

    public void flameNoti(){
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("101", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        }

        Bitmap mLargeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.fire);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:119"));
        PendingIntent pending = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(R.raw.siren);
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.title)
                .setContentTitle("집에 화재가 감지되었습니다!!")
                .setContentText("클릭시 신고화면으로 이동합니다")
                .setLargeIcon(mLargeIcon)
                .setContentIntent(pending)
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000, 500, 500, 200, 200, 200, 200, 200, 200})

                .setLights(Color.BLUE, 500, 500);

        try {
            Uri sounduri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),sounduri);
            r.play();
        }catch (Exception e){
            e.printStackTrace();
        }
        notificationManager.notify(1, notificationBuilder.build());


        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "My:Tag");
        wakeLock.acquire(5000);
    }


    public void getFlame(){
        Call<ArrayList<Dvsensor>> callSensorFire = networkService.getSensorValue(ApplicationController.user_id,"Flame");
        callSensorFire.enqueue(new Callback<ArrayList<Dvsensor>>() {
            @Override
            public void onResponse(Call<ArrayList<Dvsensor>> call, Response<ArrayList<Dvsensor>> response) {
                ArrayList<Dvsensor> dvsensorFlame = new ArrayList<>();
                dvsensorFlame = response.body();
                if(dvsensorFlame.get(0).getValue()<50){
                    flameNoti();
                }

                Log.e("제발 제발 들어와주세요 부탁입니다 플레임",response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<Dvsensor>> call, Throwable t) {

            }
        });
    }

    public void getCustomGroceryList(){
        Call<ArrayList<Grocery>> getCustomGroceryList = networkService.getGrocryList(2,ApplicationController.user_id);
        getCustomGroceryList.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                customGroceryList = new ArrayList<Grocery>();
                customGroceryList = response.body();



                for (int i = 0; i < customGroceryList.size(); i++) {
                    //유통기한입력날짜
                    String date1 = customGroceryList.get(i).getExpiration_date();
                    //현재날짜
                    Date today = new Date();
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date2 = transFormat.format(today);

                    try { // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
                        FirstDate = format.parse(date1);
                        SecondDate = format.parse(date2);

                        // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
                        // 연산결과 -950400000. long type 으로 return 된다.
                        long calDate = FirstDate.getTime() - SecondDate.getTime();

                        // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
                        // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
                        calDateDays = calDate / (24 * 60 * 60 * 1000);

                        calDateDays = Math.abs(calDateDays);
                        Log.e("유통기한 기간 몇으로 나오니?", String.valueOf(calDateDays));


                    } catch (ParseException e) {
                        // 예외 처리
                    }




                    SimpleDateFormat transFormat2 = new SimpleDateFormat("yyyyMMdd");

                    String castData1 = transFormat2.format(FirstDate);
                    String castDate2 = transFormat2.format(SecondDate);


                    if (Integer.valueOf(castData1) < Integer.valueOf(castDate2)) {
                        //알람울리게
                        dateNoti();
                    } else {

                        if (calDateDays == 0) {

                            dateNoti();
//                        알람울리게

                        } else if (calDateDays > 0 && calDateDays < 4) {


                        } else {


                        }
                    }


                    Log.e("냉장고에 커스텀 식재료 몇개 들어왔나요?", String.valueOf(customGroceryList.size()));
                    Log.e("냉장고에 커스텀 식재료 성고했나요??", String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

            }
        });
    }

    public void dateNoti(){
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("3", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        }

        Bitmap mLargeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.trash);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("datenoti","datenoti");
        PendingIntent pending = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(R.raw.siren);
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.title)
                .setContentTitle("식재료 유통기한 임박")
                .setContentText("클릭시 식재료 화면으로 이동합니다")
                .setLargeIcon(mLargeIcon)
                .setContentIntent(pending)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000, 500, 500, 200, 200, 200, 200, 200, 200})

                .setLights(Color.BLUE, 500, 500);


        notificationManager.notify(3, notificationBuilder.build());


        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "My:Tag");
        wakeLock.acquire(5000);
    }

}
