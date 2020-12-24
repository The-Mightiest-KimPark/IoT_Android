package com.example.smartrefri.grocery;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;

public class SearchViewHolder extends RecyclerView.ViewHolder{

    TextView search_item_tv;
    LinearLayout grocery_seleted_item;

    public SearchViewHolder(View itemView) {
        super(itemView);
        search_item_tv = itemView.findViewById(R.id.search_item_tv);
        grocery_seleted_item = itemView.findViewById(R.id.grocery_seleted_item);



    }
}
