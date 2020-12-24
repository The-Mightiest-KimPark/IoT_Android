package com.example.smartrefri.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import com.example.smartrefri.model.All_Grocery;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.network.NetworkService;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAddActivity extends AppCompatActivity {

    ImageView grocery_add_complete_btn;
    private static RecyclerView grocery_searched_listview;
    LinearLayoutManager layoutManager;
    EditText grocery_count_et;

    EditText grocery_date_year_et,grocery_date_month_et,grocery_date_day_et;


    TextView add_title_tv;
    ArrayList<All_Grocery> searchGroceryList, originalAdapterList;
    SearchAdapter searchAdapter;

    CustomDialog addCheckDialog,alarmCheckDialog;
    private static boolean grocery_select = false;
    private static EditText serach_grocery_et;

    Intent intent;

    NetworkService networkService;

    private static All_Grocery paramGrocery;




    CustomAddActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_add);
        activity = this;

        grocery_add_complete_btn = findViewById(R.id.grocery_add_complete_btn);
        grocery_searched_listview = findViewById(R.id.grocery_searched_listview);
        serach_grocery_et = findViewById(R.id.serach_grocery_et);
        grocery_count_et = findViewById(R.id.grocery_count_et);
        add_title_tv = findViewById(R.id.add_title_tv);

        grocery_date_year_et = findViewById(R.id.grocery_date_year_et);
        grocery_date_month_et = findViewById(R.id.grocery_date_month_et);
        grocery_date_day_et = findViewById(R.id.grocery_date_day_et);

        networkService = ApplicationController.getInstance().getNetworkService();
        paramGrocery = new All_Grocery();
        addCheckDialog = new CustomDialog(CustomAddActivity.this, BackCancelListener, BackOkListener);






      
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
                    checkCustomDialog();


            }
        });


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











    public static void selectItem(All_Grocery grocery){
        Log.e("아이디값",(String.valueOf(grocery.getId())));
        Log.e("재료이름",grocery.getName());
        serach_grocery_et.setText(grocery.getName());
        paramGrocery = grocery;
        grocery_select = true;
        grocery_searched_listview.setVisibility(View.GONE);
    }
    private View.OnClickListener BackCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addCheckDialog.dismiss();

        }

    };
    private View.OnClickListener BackOkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.e("값넣기전 식재료id",String.valueOf(paramGrocery.getId()));
            Log.e("값넣기전 식재료갯수",grocery_count_et.getText().toString());
            Log.e("값넣기전 식재료 이름",paramGrocery.getName());



            if (serach_grocery_et.length() ==0 || grocery_select==false ) {
                Toast.makeText(getApplicationContext(), "재료를 검색 후 선택해주세요!", Toast.LENGTH_SHORT).show();
            }else if(grocery_count_et.length() == 0){
                Toast.makeText(getApplicationContext(), "수량을 입력해주세요!", Toast.LENGTH_SHORT).show();
            }else{


                if(grocery_date_year_et.length() == 0 || grocery_date_month_et.length() == 0 || grocery_date_day_et.length() ==0){
                    Log.e("유통기한널값","여기인가요 졸려죽어");
                    addCustomGrocery(null);
                }else{

                    Log.e("유통기한확인",grocery_date_year_et.getText().toString());
                    Log.e("유통기한확인",grocery_date_month_et.getText().toString());
                    Log.e("유통기한확인",grocery_date_day_et.getText().toString());

                    String time = grocery_date_year_et.getText().toString()+"-"+grocery_date_month_et.getText().toString() +"-"+grocery_date_day_et.getText().toString();



                    addCustomGrocery(time);

                }
                //서버로 식재료 값 넣어주기

                //서버에 성공적으로 넣는 것이 성공하면 액티비티 화면 종료


                addCheckDialog.dismiss();
                finish();
            }
        }
    };
    public void checkCustomDialog(){
        addCheckDialog.show();

        addCheckDialog.setTitle("재료 추가");
        addCheckDialog.setContent("재료를 추가하시겠습니까?");
    }



    public void addCustomGrocery(String time){
        Log.e("값넣기전 식재료id",String.valueOf(paramGrocery.getId()));
        Log.e("값넣기전 식재료갯수",grocery_count_et.getText().toString());
        Log.e("값넣기전 식재료 이름",paramGrocery.getName());
        Call<Void> addGrocery = networkService.addGrocery(new Grocery(ApplicationController.user_id,paramGrocery.getId(),paramGrocery.getName(),Integer.valueOf(grocery_count_et.getText().toString()),time));
        addGrocery.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.e("됐나요?","식재료 추가됐나요?");
                Log.e("됐나요?",String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
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
                searchAdapter = new SearchAdapter(searchGroceryList,getApplication(),activity);
                grocery_searched_listview.setVisibility(View.GONE);



            }

            @Override
            public void onFailure(Call<ArrayList<All_Grocery>> call, Throwable t) {

            }
        });
    }
}