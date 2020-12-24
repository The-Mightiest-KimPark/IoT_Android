package com.example.smartrefri.follow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.GroceryAdapter;
import com.example.smartrefri.main.StoryAdapter;
import com.example.smartrefri.model.Follow;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFollow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFollow extends Fragment {

    //뷰 변수 선언
    RecyclerView follow_list_rv;
    EditText follow_user_email_et;
    Button follow_add_btn;

    //리사이클러뷰
    LinearLayoutManager layoutManager;
    FollowAdapter followAdapter;
    ArrayList<Follow> followList;
    
    //네트워크
    NetworkService networkService;

    ArrayList<Follow> myfollowList;

    FragmentFollow fragmentFollow;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentFollow() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFollow.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFollow newInstance(String param1, String param2) {
        FragmentFollow fragment = new FragmentFollow();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //서버통신
        fragmentFollow = this;

        networkService = ApplicationController.getInstance().getNetworkService();


    }

    @Override
    public void onResume() {
        super.onResume();
        getFollowList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_follow, container, false);

        follow_list_rv = v.findViewById(R.id.follow_list_rv);
        follow_user_email_et = v.findViewById(R.id.follow_user_email_et);
        follow_add_btn = v.findViewById(R.id.follow_add_btn);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        follow_list_rv.setLayoutManager(layoutManager);

        getFollowList();





        follow_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                putFollow(String.valueOf(follow_user_email_et.getText()));

                //서버통신(성공하면 추가되었다는 통신과 실패하면 등록되지 않은 사용자입니다 토스트 띄워주기). 그리고 서버와 통신후 리사이클러뷰 ㅅ갱신
            }
        });

        return v;
    }

    public void putFollow(String followemail){
        networkService.putFollow(new Follow(ApplicationController.user_id,followemail)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "팔로우가 추가되었습니다!", Toast.LENGTH_SHORT).show();
                    onResume();
                }else{
                    Toast.makeText(getContext(), "등록되지 않은 사용자입니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });

        
    }
    public void getFollowList(){
        Call<ArrayList<Follow>> getFollowList = networkService.getFollowList(ApplicationController.user_id);
        getFollowList.enqueue(new Callback<ArrayList<Follow>>() {
            @Override
            public void onResponse(Call<ArrayList<Follow>> call, Response<ArrayList<Follow>> response) {
                myfollowList = new ArrayList<Follow>();
                myfollowList = response.body();

                followAdapter = new FollowAdapter(myfollowList,getContext(),fragmentFollow);
                follow_list_rv.setAdapter(followAdapter);




                Log.e("제발 제발 들어와주세요 부탁입니다 팔로우",response.message());
            }

            @Override
            public void onFailure(Call<ArrayList<Follow>> call, Throwable t) {

            }
        });
    }




}