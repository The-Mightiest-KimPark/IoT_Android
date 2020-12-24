package com.example.smartrefri.mypage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartrefri.R;
import com.example.smartrefri.Recipe.FragmentRecipeDetail;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.follow.FragmentFollow;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.User;
import com.example.smartrefri.network.NetworkService;
import com.example.smartrefri.signin.SignInActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMyPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMyPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView service_test;
    LinearLayout mypage_follow_btn;
    LinearLayout mypage_logout_btn;
    TextView mypage_name_tv,mypage_age_tv,mypage_sex_tv,mypage_email_tv,mypage_phone_tv,mypage_guardian_name_tv,mypage_guardian_phone_tv,mypage_purpose_tv;
    ImageView mypage_photo_iv;
    boolean start=false;


    FragmentTransaction transaction;

    Fragment followFragment = new FragmentFollow();

    SharedPreferences loginInformation;
    SharedPreferences.Editor editor;

    NetworkService networkService;

    private static User userInfo;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMyPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMyPage.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMyPage newInstance(String param1, String param2) {
        FragmentMyPage fragment = new FragmentMyPage();
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
        transaction = getFragmentManager().beginTransaction();
        networkService = ApplicationController.getInstance().getNetworkService();

        loginInformation = getActivity().getSharedPreferences("setting",MODE_PRIVATE);
        editor = loginInformation.edit();
    }

    @Override
    public void onResume() {
        super.onResume();


        getUserInfo();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        service_test= view.findViewById(R.id.service_test);
        mypage_follow_btn = view.findViewById(R.id.mypage_follow_btn);
        mypage_logout_btn = view.findViewById(R.id.mypage_logout_btn);
        mypage_name_tv = view.findViewById(R.id.mypage_name_tv);
        mypage_photo_iv = view.findViewById(R.id.mypage_photo_iv);

        mypage_age_tv = view.findViewById(R.id.mypage_age_tv);
        mypage_sex_tv = view.findViewById(R.id.mypage_sex_tv);
        mypage_email_tv=view.findViewById(R.id.mypage_email_tv);
        mypage_phone_tv = view.findViewById(R.id.mypage_phone_tv);
        mypage_guardian_name_tv=view.findViewById(R.id.mypage_guardian_name_tv);
        mypage_guardian_phone_tv = view.findViewById(R.id.mypage_guardian_phone_tv);
        mypage_purpose_tv=view.findViewById(R.id.mypage_purpose_tv);








        service_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start==false) {
                    start=true;
                    Toast.makeText(getContext(), "알림이 설정 되었습니다", Toast.LENGTH_SHORT).show();
                    Log.d("test", "액티비티-서비스 시작버튼클릭");
                    Intent intent = new Intent(getContext(), AlarmService.class); // 이동할 컴포넌트
                    service_test.setImageResource(R.drawable.notion);
                    getContext().startService(intent); // 서비스 시작

                }else {
                    start=false;
                    service_test.setImageResource(R.drawable.notioff);
                    Toast.makeText(getContext(), "알림이 해제 되었습니다", Toast.LENGTH_SHORT).show();
                    Log.d("test", "액티비티-서비스 종료버튼클릭");
                    Intent intent = new Intent(getContext(),AlarmService.class); // 이동할 컴포넌트
                    getContext().stopService(intent); // 서비스 종료

                }


            }

        });

        mypage_follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("RecipeValue",recipe);
//                recipe_detail.setArguments(bundle);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout,followFragment);
                transaction.commit();

            }
        });

        mypage_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApplicationController.user_id = "";
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();


            }
        });

        return view;
    }

    public void getUserInfo(){
        Call<User> getUserInfo = networkService.getUserInfo(ApplicationController.user_id);
        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    userInfo = new User();
                    userInfo = response.body();
                    Log.e("혹시여기로 들어왔니?",String.valueOf(response.code()));
                    Log.e("유저값",userInfo.getName());

                    mypage_name_tv.setText(userInfo.getName());
                    if(userInfo.getImg_url()==null){

                        if(userInfo.getSex()==1){
                            Glide.with(getContext())
                                    .load(R.drawable.male)
                                    .into(mypage_photo_iv);
                        }else if(userInfo.getSex()==2){
                            Glide.with(getContext())
                                    .load(R.drawable.female)
                                    .into(mypage_photo_iv);
                        }
                    }else{
                        Glide.with(getContext())
                                .load(userInfo.getImg_url())
                                .into(mypage_photo_iv);
                    }

                    mypage_age_tv.setText(String.valueOf(userInfo.getAge()));
                    if(userInfo.getSex()==1){
                        mypage_sex_tv.setText("남자");
                    }else if(userInfo.getSex()==2){
                        mypage_sex_tv.setText("여자");
                    }


                    mypage_email_tv.setText(userInfo.getEmail());
                    mypage_phone_tv.setText(userInfo.getPhone_number());
                    mypage_guardian_name_tv.setText(userInfo.getGuardian_name());
                    mypage_guardian_phone_tv.setText(userInfo.getGuardian_phone_number());
                    mypage_purpose_tv.setText(userInfo.getPurpos());


                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.e("애초에 여기서 에러인가요?",t.getMessage());
            }
        });
    }
}