

package com.example.smartrefri.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.model.Follow;
import com.example.smartrefri.model.User;
import com.example.smartrefri.network.NetworkService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    ArrayList<Follow> followUserList;
    private Context context;
    Intent intent;

    NetworkService networkService;
    int itemPosition;

    public StoryAdapter(Context context, ArrayList<Follow> followList) {
        this.context = context;
        followUserList = followList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("스토리어댑터","들어오지요?");

        // context 와 parent.getContext() 는 같다.
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        networkService = ApplicationController.getInstance().getNetworkService();

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


            // 사진 이미 읽었을때 테두리 회색으로바꾸기
            if(followUserList.get(position).isRead()){
                holder.circleImageView.setBorderColor(Color.GRAY);
                if(followUserList.get(position).img_url==null){
                    if(followUserList.get(position).getSex()==1){
                        Glide.with(context)
                                .load(R.drawable.male)
                                .into(holder.circleImageView);
                    }else if(followUserList.get(position).getSex()==2){
                        Glide.with(context)
                                .load(R.drawable.female)
                                .into(holder.circleImageView);
                    }

                }else{
                    Glide.with(context)
                            .load(followUserList.get(position).img_url)
                            .into(holder.circleImageView);
                }
                holder.name_TextView.setText(followUserList.get(position).getName());
            }else{
                if(followUserList.get(position).img_url==null){
                    if(followUserList.get(position).getSex()==1){
                        Glide.with(context)
                                .load(R.drawable.male)
                                .into(holder.circleImageView);
                    }else if(followUserList.get(position).getSex()==2){
                        Glide.with(context)
                                .load(R.drawable.female)
                                .into(holder.circleImageView);
                    }

                }else{
                    Glide.with(context)
                            .load(followUserList.get(position).img_url)
                            .into(holder.circleImageView);
                }
                holder.name_TextView.setText(followUserList.get(position).getName());

            }

            //            holder.circleImageView.setBorderColor();
            holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemPosition = position;
                    Log.e("상대방 계정 아이디",followUserList.get(itemPosition).getEmail());
                    readFollow();

                }
            });


    }


    @Override
    public int getItemCount() {
        return followUserList.size();
    }


    public void readFollow(){
        Call<Void> putStoryRead = networkService.putStoryRead(new Follow(ApplicationController.user_id,followUserList.get(itemPosition).getEmail()));
        putStoryRead.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("읽음 성공",String.valueOf(response.code()));
                Log.e("읽음 성공메세지",response.message());
                intent = new Intent(context, StoryPicActivity.class);
                intent.putExtra("username",followUserList.get(itemPosition).getName());
                intent.putExtra("sex",followUserList.get(itemPosition).getSex());
                intent.putExtra("userImage",followUserList.get(itemPosition).getImg_url());
                intent.putExtra("refriImage",followUserList.get(itemPosition).getUrl());
                intent.putExtra("reg_date",followUserList.get(itemPosition).getReg_date());

                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }




}
