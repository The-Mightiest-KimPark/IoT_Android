package com.example.smartrefri.grocery;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.CustomDialog;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.All_Grocery;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryViewHolder> {

    private Context context;
    private ArrayList<Grocery> grocerylist;
    FragmentGrocery fragmentGrocery;
    MainActivity mainActivity;
    CustomDialog deleteCheckDialog;

    NetworkService networkService;
    Date FirstDate;
    Date SecondDate;

    int selectItem;
    long calDateDays;

    public GroceryAdapter(ArrayList<Grocery> allGrocerylist, Context context, FragmentGrocery fragmentGrocery, MainActivity mainActivity) {
        this.grocerylist = allGrocerylist;
        this.context = context;
        this.fragmentGrocery = fragmentGrocery;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_grocery, parent, false);
        GroceryViewHolder viewHolder = new GroceryViewHolder(itemView);
        networkService = ApplicationController.getInstance().getNetworkService();
        deleteCheckDialog = new CustomDialog(mainActivity, BackCancelListener, BackOkListener);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return grocerylist.size();
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {

        holder.grocery_name_tv.setText(grocerylist.get(position).getName());
        holder.grocery_count_tv.setText(String.valueOf(grocerylist.get(position).getCount()));


        if (grocerylist.get(position).getExpiration_date() == null) {
            holder.grocery_date_tv.setVisibility(View.GONE);
        } else {

            //유통기한 입력날짜 - 현재 날짜

            //유통기한입력날짜
            String date1 = grocerylist.get(position).getExpiration_date();
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
                Log.e("유통기한 기간 몇으로 나오니?",String.valueOf(calDateDays));


            } catch (ParseException e) {
                // 예외 처리
            }

            holder.grocery_date_tv.setText(grocerylist.get(position).getExpiration_date());



            SimpleDateFormat transFormat2 = new SimpleDateFormat("yyyyMMdd");

            String castData1 = transFormat2.format(FirstDate);
            String castDate2 = transFormat2.format(SecondDate);



            if(Integer.valueOf(castData1) < Integer.valueOf(castDate2)){
                holder.grocery_date_tv.setTextColor(Color.RED);
                holder.grocery_date_tv.setText(grocerylist.get(position).getExpiration_date()+"(폐기요망)");
            }else{

                if (calDateDays == 0) {

                    holder.grocery_date_tv.setTextColor(Color.RED);

                } else if (calDateDays >0 && calDateDays <4) {

                    holder.grocery_date_tv.setTextColor(Color.BLUE);

                } else {

                    holder.grocery_date_tv.setTextColor(Color.BLACK);
                }
            }






            }

        if (grocerylist.get(position).getGubun() == 2) {
            holder.grocery_list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("클릭", "한번 클릭:수정");

//                fragmentGrocery.fragmentRefresh();
                }
            });
            holder.grocery_list_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e("클릭", "길게 클릭:삭제");

                    selectItem = position;
                    checkCustomDialog();


                    return false;
                }
            });
        }


    }

    public void deleteGrocery() {
        Log.e("삭제전 id값 확인",String.valueOf(grocerylist.get(selectItem).getGrocery_id()));

        Call<Void> deleteGrocery = networkService.deleteGrocery(ApplicationController.user_id, grocerylist.get(selectItem).getGrocery_id());
        deleteGrocery.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("삭제 상태", String.valueOf(response.code()));
                Log.e("삭제 결과", response.message());
                //프래그먼트 새로고침
                if(response.isSuccessful()){
                    fragmentGrocery.fragmentRefresh();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void checkCustomDialog() {
        deleteCheckDialog.show();

        deleteCheckDialog.setTitle("식재료 삭제");
        deleteCheckDialog.setContent("재료를 삭제하시겠습니까?");
    }

    private View.OnClickListener BackCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            deleteCheckDialog.dismiss();

        }

    };
    private View.OnClickListener BackOkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            deleteCheckDialog.dismiss();
            //삭제 서버 통신
            deleteGrocery();


        }
    };

}

