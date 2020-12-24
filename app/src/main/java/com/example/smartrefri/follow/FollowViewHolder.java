package com.example.smartrefri.follow;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrefri.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowViewHolder extends RecyclerView.ViewHolder {
    CircleImageView follow_user_iv;
    TextView follow_user_name_tv,follow_user_email_tv;
    LinearLayout follow_item_ll;


    public FollowViewHolder(@NonNull View itemView)

    {
        super(itemView);
        follow_user_iv = itemView.findViewById(R.id.follow_user_iv);
        follow_user_name_tv = itemView.findViewById(R.id.follow_user_name_tv);
        follow_user_email_tv = itemView.findViewById(R.id.follow_user_email_tv);
        follow_item_ll=itemView.findViewById(R.id.follow_item_ll);

    }
}
