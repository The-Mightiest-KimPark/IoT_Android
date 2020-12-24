package com.example.smartrefri.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartrefri.CustomDialog;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.model.Alarm;
import com.example.smartrefri.model.All_Grocery;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmAddActivity extends AppCompatActivity {

    ImageView grocery_add_complete_btn;
    private static RecyclerView grocery_searched_listview;
    LinearLayoutManager layoutManager;
    EditText grocery_count_et;
    TextView add_title_tv;
    ArrayList<All_Grocery> searchGroceryList, originalAdapterList;
    SearchAdapter searchAdapter;

    CustomDialog alarmCheckDialog;
    private static boolean grocery_select = false;
    private static EditText serach_grocery_et;

    Intent intent;

    NetworkService networkService;

    private static All_Grocery paramGrocery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        alarmCheckDialog = new CustomDialog(AlarmAddActivity.this, CancelListener, OkListener);


        grocery_add_complete_btn = findViewById(R.id.grocery_add_complete_btn);
        grocery_searched_listview = findViewById(R.id.grocery_searched_listview);
        serach_grocery_et = findViewById(R.id.serach_grocery_et);
        grocery_count_et = findViewById(R.id.grocery_count_et);
        add_title_tv = findViewById(R.id.add_title_tv);

        networkService = ApplicationController.getInstance().getNetworkService();
        paramGrocery = new All_Grocery();



        SearchData();

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        serach_grocery_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                grocery_select = false;
                grocery_searched_listview.setVisibility(View.VISIBLE);
                grocery_searched_listview.setAdapter(searchAdapter);
                String text = serach_grocery_et.getText().toString();
                search(text);
            }
        });

        grocery_add_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarmDialog();


            }
        });


    }

    public static void selectItem(All_Grocery grocery){
        Log.e("아이디값",(String.valueOf(grocery.getId())));
        Log.e("재료이름",grocery.getName());
        serach_grocery_et.setText(grocery.getName());
        paramGrocery = grocery;
        grocery_select = true;
        grocery_searched_listview.setVisibility(View.GONE);
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        searchGroceryList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
//            searchGroceryList.addAll(originalAdapterList);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < originalAdapterList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (originalAdapterList.get(i).getName().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    searchGroceryList.add(originalAdapterList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        searchAdapter.notifyDataSetChanged();
    }

    //식재료 알림 추가
    private View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            alarmCheckDialog.dismiss();

        }

    };
    private View.OnClickListener OkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (serach_grocery_et.length() ==0 || grocery_select==false ) {
                Toast.makeText(getApplicationContext(), "재료를 검색 후 선택해주세요!", Toast.LENGTH_SHORT).show();
            }else if(grocery_count_et.length() == 0){
                Toast.makeText(getApplicationContext(), "수량을 입력해주세요!", Toast.LENGTH_SHORT).show();
            }else{

                //서버로 식재료 값 넣어주기
                addAlarmGrocery();
                //서버에 성공적으로 넣는 것이 성공하면 액티비티 화면 종료
                alarmCheckDialog.dismiss();
                finish();
            }
        }
    };


    public void addAlarmGrocery(){

        Call<Void> addGroceryAlarm = networkService.addGroceryAlarm(new Alarm(ApplicationController.user_id,paramGrocery.getId(),Integer.valueOf(grocery_count_et.getText().toString())));
        addGroceryAlarm.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("식재료 알림 추가",String.valueOf(response.code()));
                Log.e("식재료 알림추가메세지",response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    public void addAlarmDialog() {
        alarmCheckDialog.show();
        alarmCheckDialog.setTitle("재료 알림 설정");
        alarmCheckDialog.setContent("알림을 설정하시겠습니까?");

    }

    public void SearchData(){
        Call<ArrayList<All_Grocery>> getALLGroceryList = networkService.getALLGroceryList();
        getALLGroceryList.enqueue(new Callback<ArrayList<All_Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<All_Grocery>> call, Response<ArrayList<All_Grocery>> response) {
                //전체 식료품 리스트 받아오기
                searchGroceryList = new ArrayList<All_Grocery>();
                searchGroceryList = response.body();

                Log.e("검색창 식재료값 받아와졌나요",String.valueOf(response.code()));

                //복사본 생성  리스트
                originalAdapterList = new ArrayList<All_Grocery>();
                originalAdapterList.addAll(searchGroceryList);

                layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                grocery_searched_listview.setLayoutManager(layoutManager);
                //리사이클러뷰에 어댑터 생성 & 연결
                searchAdapter = new SearchAdapter(searchGroceryList,getApplication(),AlarmAddActivity.this);
                grocery_searched_listview.setVisibility(View.GONE);



            }

            @Override
            public void onFailure(Call<ArrayList<All_Grocery>> call, Throwable t) {

            }
        });
    }





}
