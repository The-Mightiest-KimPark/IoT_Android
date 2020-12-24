package com.example.smartrefri.grocery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.Coordinate;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGrocery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGrocery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView grocery_add_btn,grocery_bell_btn;
    LinearLayout grocery_smart_drop_btn, grocery_custom_drop_btn;

    RecyclerView grocery_smart_rv, grocery_custom_rv;
    LinearLayoutManager smart_layoutManager, customm_layoutManager;
    GroceryAdapter smartAdapter, customAdapter;
    ArrayList<Grocery> smartGroceryList, customGroceryList;

    MainActivity activity;
    Intent intent;

    //네트워크
    NetworkService networkService;

    FragmentGrocery fragmentGrocery;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentGrocery() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGrocery.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGrocery newInstance(String param1, String param2) {
        FragmentGrocery fragment = new FragmentGrocery();
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
        Log.e("여기로 새롭게 들어와야만해1","맞오?");
        networkService = ApplicationController.getInstance().getNetworkService();
        fragmentGrocery = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        //여기서 네트워크 값 받아오기
        //setText도 여기서해주기
        getSmartGroceryList();
        getCustomGroceryList();
        
        Log.e("여기로 새롭게 들어와야만해2","맞오?");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("혹시 여기로 들어오니?","맞오?");

    }

    public void fragmentRefresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grocery, container, false);
//        여기는 파인드뷰바이아이디만해주기
        grocery_add_btn = v.findViewById(R.id.grocery_add_btn);
        grocery_smart_drop_btn = v.findViewById(R.id.grocery_smart_drop_btn);
        grocery_custom_drop_btn = v.findViewById(R.id.grocery_custom_drop_btn);
        grocery_bell_btn = v.findViewById(R.id.grocery_bell_btn);

        grocery_smart_rv = v.findViewById(R.id.grocery_smart_rv);
        grocery_custom_rv = v.findViewById(R.id.grocery_custom_rv);

        smart_layoutManager = new LinearLayoutManager(getContext());
        smart_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grocery_smart_rv.setLayoutManager(smart_layoutManager);

        customm_layoutManager = new LinearLayoutManager(getContext());
        customm_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grocery_custom_rv.setLayoutManager(customm_layoutManager);

        Log.e("여기로 새롭게 들어와야만해3","맞오?");
        grocery_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), CustomAddActivity.class);
                startActivity(intent);

                Log.e("인텐트 되고있니?","맞오?");
            }
        });
        grocery_bell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), GroceryAlarmActivity.class);
                startActivity(intent);
                Log.e("인텐트 되고있니?2222","맞오?");
            }
        });
        grocery_smart_drop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(grocery_smart_rv.getVisibility()==View.VISIBLE){
                    grocery_smart_rv.setVisibility(View.GONE);
                }else if(grocery_smart_rv.getVisibility()==View.GONE){
                    grocery_smart_rv.setVisibility(View.VISIBLE);
                }
            }
        });
        grocery_custom_drop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(grocery_custom_rv.getVisibility()==View.VISIBLE){
                    grocery_custom_rv.setVisibility(View.GONE);
                }else if(grocery_custom_rv.getVisibility()==View.GONE){
                    grocery_custom_rv.setVisibility(View.VISIBLE);
                    onResume();
                }
            }
        });


        //구분1 ai 이미지로 저장된 값 불러와서 넣기
        getSmartGroceryList();
        getCustomGroceryList();


        return v;
    }

    public void getSmartGroceryList(){
        Call<ArrayList<Grocery>> getSmartGroceryList = networkService.getGrocryList(1,ApplicationController.user_id);
        getSmartGroceryList.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                smartGroceryList = new ArrayList<Grocery>();
                smartGroceryList = response.body();
                smartAdapter = new GroceryAdapter(smartGroceryList,getContext(),fragmentGrocery,activity);
                grocery_smart_rv.setAdapter(smartAdapter);
                Log.e("냉장고에 스마트 식재료 몇개 들어왔나요?",String.valueOf(smartGroceryList.size()));
                Log.e("냉장고에 스마트 식재료 성고했나요??",String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

            }
        });
    }
    public void getCustomGroceryList(){
        Call<ArrayList<Grocery>> getCustomGroceryList = networkService.getGrocryList(2,ApplicationController.user_id);
        getCustomGroceryList.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                customGroceryList = new ArrayList<Grocery>();
                customGroceryList = response.body();
                customAdapter = new GroceryAdapter(customGroceryList,getContext(),fragmentGrocery,activity);
                grocery_custom_rv.setAdapter(customAdapter);
                Log.e("냉장고에 커스텀 식재료 몇개 들어왔나요?",String.valueOf(customGroceryList.size()));
                Log.e("냉장고에 커스텀 식재료 성고했나요??",String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

            }
        });
    }


}