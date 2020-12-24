package com.example.smartrefri.grocery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;
import com.example.smartrefri.main.ViewHolder;
import com.example.smartrefri.model.All_Grocery;
import com.example.smartrefri.model.Grocery;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private Context context;
    private ArrayList<All_Grocery> allGrocerylist;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    private String what;


    CustomAddActivity customAddActivity;
    AlarmAddActivity alarmAddActivity;

    int a;

    public SearchAdapter(ArrayList<All_Grocery> allGrocerylist, Context context,CustomAddActivity customAddActivity){
        this.allGrocerylist = allGrocerylist;
        this.context = context;
        this.customAddActivity = customAddActivity;
        a = 0;

    }
    public SearchAdapter(ArrayList<All_Grocery> allGrocerylist, Context context,AlarmAddActivity alarmAddActivity){
        this.allGrocerylist = allGrocerylist;
        this.context = context;
        this.alarmAddActivity = alarmAddActivity;
        a=1;

    }



    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // context 와 parent.getContext() 는 같다.
        View itemView = LayoutInflater.from(context).inflate(R.layout.grocery_search_item, parent, false);
        SearchViewHolder viewHolder = new SearchViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.search_item_tv.setText(allGrocerylist.get(position).getName());
        holder.grocery_seleted_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(a==0) {
                        customAddActivity.selectItem(allGrocerylist.get(position));
                    }
                    if(a==1) {
                        alarmAddActivity.selectItem(allGrocerylist.get(position));
                    }
//                Log.e("선택할때 객체에 값 잘 들어있나요?",allGrocerylist.get(position).getName());
//                Log.e("선택할때 객체에 값 잘 들어있나요?",String.valueOf(allGrocerylist.get(position).getId()));


            }
        });


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return allGrocerylist.size();
    }


}
