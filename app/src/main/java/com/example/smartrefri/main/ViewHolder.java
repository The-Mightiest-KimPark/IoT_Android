package com.example.smartrefri.main;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;

import java.text.BreakIterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder{
    CircleImageView circleImageView;
    TextView name_TextView;

    public ViewHolder(View itemView) {
        super(itemView);

        circleImageView = (CircleImageView)itemView.findViewById(R.id.story_circle);
        name_TextView = (TextView)itemView.findViewById(R.id.username_tv);



    }

}
