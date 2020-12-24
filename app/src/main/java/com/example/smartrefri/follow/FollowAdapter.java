package com.example.smartrefri.follow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.GroceryViewHolder;
import com.example.smartrefri.model.Follow;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FollowAdapter extends RecyclerView.Adapter<FollowViewHolder>{

    private Context context;
    private ArrayList<Follow> followList = new ArrayList<Follow>();
    NetworkService networkService;
    FragmentFollow fragmentFollow;

    public FollowAdapter(ArrayList<Follow> followList, Context context,FragmentFollow fragmentFollow){
        this.followList = followList;
        this.context = context;
        this.fragmentFollow = fragmentFollow;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        FollowViewHolder viewHolder = new FollowViewHolder(itemView);
        networkService = ApplicationController.getInstance().getNetworkService();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {


        if(followList.get(position).getImg_url()==null){
            if(followList.get(position).getSex()==1){
                Glide.with(context)
                        .load(R.drawable.male)
                        .into(holder.follow_user_iv);
            }else{
                Glide.with(context)
                        .load(R.drawable.female)
                        .into(holder.follow_user_iv);
            }
        }

        holder.follow_user_email_tv.setText(followList.get(position).getEmail());
        holder.follow_user_name_tv.setText(followList.get(position).getName());
        holder.follow_item_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //서버에서 통신 코드 넣기
                putFollow(followList.get(position).getEmail());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    public void putFollow(String followemail){
        networkService.putFollow(new Follow(ApplicationController.user_id,followemail)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(context, "팔로우가 해제되었습니다", Toast.LENGTH_SHORT).show();
                    fragmentFollow.onResume();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });


    }
}
