package com.example.smartrefri.grocery;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;

public class GroceryViewHolder extends RecyclerView.ViewHolder{
    TextView grocery_name_tv,grocery_count_tv,grocery_date_tv;
    LinearLayout grocery_list_item;

    public GroceryViewHolder(View itemView) {

        super(itemView);

        grocery_name_tv = itemView.findViewById(R.id.grocery_name_tv);
        grocery_count_tv = itemView.findViewById(R.id.grocery_count_tv);
        grocery_date_tv = itemView.findViewById(R.id.grocery_date_tv);
        grocery_list_item = itemView.findViewById(R.id.grocery_list_item);
    }
}
