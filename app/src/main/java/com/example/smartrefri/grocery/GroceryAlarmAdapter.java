package com.example.smartrefri.grocery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.CustomDialog;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.Alarm;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceryAlarmAdapter extends RecyclerView.Adapter<GroceryAlarmViewHolder>{

    private Context context;
    private ArrayList<Alarm> groceryAlarmList;

    CustomDialog deleteCheckDialog;

    NetworkService networkService;

    GroceryAlarmActivity groceryAlarmActivity;

    private static int selectitem;

    public GroceryAlarmAdapter(ArrayList<Alarm> groceryAlarmList, Context context,GroceryAlarmActivity groceryAlarmActivity){
        this.groceryAlarmList = groceryAlarmList;
        this.context = context;
        this.groceryAlarmActivity = groceryAlarmActivity;
    }

    @NonNull
    @Override
    public GroceryAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_alarm_grocery, parent, false);
        GroceryAlarmViewHolder viewHolder = new GroceryAlarmViewHolder(itemView);
        deleteCheckDialog = new CustomDialog(groceryAlarmActivity, BackCancelListener, BackOkListener);
        networkService = ApplicationController.getInstance().getNetworkService();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryAlarmViewHolder holder, int position) {
        holder.grocery_alarm_name_tv.setText(groceryAlarmList.get(position).getName());
        holder.grocery_grocery_count_tv.setText(String.valueOf(groceryAlarmList.get(position).getCount()));

        holder.grocery_alarm_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.grocery_alarm_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectitem = position;
                checkCustomDialog();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return groceryAlarmList.size();
    }

    public void deleteGrocery() {
        Log.e("삭제전 id값 확인",String.valueOf(groceryAlarmList.get(selectitem).getAll_grocery_id()));

        Call<Void> deleteGroceryAlarm = networkService.deleteGroceryAlarm(ApplicationController.user_id,groceryAlarmList.get(selectitem).getAll_grocery_id());
        deleteGroceryAlarm.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("삭제 상태", String.valueOf(response.code()));
                Log.e("삭제 결과", response.message());
                groceryAlarmActivity.onResume();


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("안되는 이유", t.getMessage());

            }
        });
    }

    public void checkCustomDialog() {
        deleteCheckDialog.show();

        deleteCheckDialog.setTitle("식재료 알람 삭제");
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
