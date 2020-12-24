package com.example.smartrefri.grocery;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;

public class GroceryAlarmViewHolder extends RecyclerView.ViewHolder {

    TextView grocery_alarm_name_tv,grocery_grocery_count_tv;
    LinearLayout grocery_alarm_item;

    public GroceryAlarmViewHolder(View itemView) {

        super(itemView);

        grocery_alarm_name_tv = itemView.findViewById(R.id.grocery_alarm_name_tv);
        grocery_grocery_count_tv = itemView.findViewById(R.id.grocery_grocery_count_tv);
        grocery_alarm_item = itemView.findViewById(R.id.grocery_alarm_item);
    }
}
