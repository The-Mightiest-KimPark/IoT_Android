package com.example.smartrefri.Refri;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.grocery.FragmentGrocery;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.model.Icon;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRefri#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRefri extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Fragment grocery_list = new FragmentGrocery();
    ImageView grocery_list_btn;


    RelativeLayout refri_in;
    FragmentTransaction transaction;
    CircleImageView refri_control_btn;

    ArrayList<Icon> iconList;

    NetworkService networkService;

    float preX;
    float preY;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRefri() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFamily.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRefri newInstance(String param1, String param2) {
        FragmentRefri fragment = new FragmentRefri();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_refri, container, false);
        grocery_list_btn = v.findViewById(R.id.grocery_list_btn);
        refri_in = v.findViewById(R.id.refri_in);
        refri_control_btn = v.findViewById(R.id.refri_control_btn);

        getGrocery();
//
//
//        ArrayList<Double[]> coordinate1 = new ArrayList<Double[]>();
//        coordinate1.add(new Double[]{192.48219299316406,128.51637268066406});
//
//        ArrayList<Double[]> coordinate2 = new ArrayList<Double[]>();
//        coordinate2.add(new Double[]{123.17382049560547,372.5484924316406});
//
//        ArrayList<Double[]> coordinate3 = new ArrayList<Double[]>();
//        coordinate3.add(new Double[]{307.1622619628906,259.3542175292969});
//
//        ArrayList<Double[]> coordinate4 = new ArrayList<Double[]>();
//        coordinate4.add(new Double[]{259.3607482910156,447.88714599609375});
//
//        ArrayList<Double[]> coordinate5 = new ArrayList<Double[]>();
//        coordinate5.add(new Double[]{399.00872802734375,239.72467041015625});
//
//
//
//
//
//        groceries.add(new Grocery(1,"wusldd@naver.com",2,"레몬",1,"날짜",1,coordinate1));
//        groceries.add(new Grocery(2,"wusldd@naver.com",4,"오이",1,"날짜",1,coordinate2));
//        groceries.add(new Grocery(3,"wusldd@naver.com",5,"사이다",1,"날짜",1,coordinate3));
//        groceries.add(new Grocery(4,"wusldd@naver.com",16,"가지",1,"날짜",1,coordinate4));
//        groceries.add(new Grocery(5,"wusldd@naver.com",23,"콜라",1,"날짜",1,coordinate5));
//
//



        grocery_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout,grocery_list);
                transaction.commit();
            }
        });

        refri_control_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),ControlActivity.class);
                startActivity(intent);
            }
        });

        return v;

}

    public void getGrocery(){
        Call<ArrayList<Grocery>> getGrocryList = networkService.getGrocryList(1,ApplicationController.user_id);
        getGrocryList.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                ArrayList<Grocery> groceries = new ArrayList<Grocery>();
                groceries = response.body();

                Log.e("냉장고에 식재료 몇개 들어왔나요?",String.valueOf(groceries.size()));
                Log.e("냉장고에 식재료 몇개 들어왔나요?",String.valueOf(groceries.size()));

                preX=0;
                preY=0;


                for(int i=0;i<groceries.size();i++){
                    Log.e("냉장고에 식재료 좌표값몇개 들어왔나요?",String.valueOf(groceries.get(i).getcoordinate().size()));
                    for(int j=0;j<groceries.get(i).getcoordinate().size();j++){
                        double x= groceries.get(i).getcoordinate().get(j)[0];
                        double y=groceries.get(i).getcoordinate().get(j)[1];

                        ImageView iv = new ImageView(getContext());
                        iv.setLayoutParams(new LinearLayout.LayoutParams(270, 350));

                        refri_icon();

                        Log.e("이미지 띄우는 부분인데 안뜨나요?",String.valueOf(groceries.get(i).getGrocery_id()));

                        for(int z=0; z<iconList.size();z++){ 
                            Log.e("이미지 띄우는 직전인데?",String.valueOf(iconList.size()));
                            if(iconList.get(z).getAll_grocery_id() == groceries.get(i).getGrocery_id()){
                                Log.e("이미지 띄우는 부분인데 안뜨나요?","말해봐요");
                                int ab = groceries.get(i).getGrocery_id()-1;
                                iv.setImageResource(iconList.get(ab).getUrl());
                            }
                        }


                        iv.setX((float)60+(float)x);
                        iv.setY((float)y+(float)80);
                        preX=((float)20)*(float)(i+1);
                        preY=(float)y;
                        refri_in.addView(iv);

                    }
                }

                preX=0;
                preY=0;
                for(int i=0;i<groceries.size();i++){
                    for(int j=0;j<groceries.get(i).getcoordinate().size();j++){
                        double x= groceries.get(i).getcoordinate().get(j)[0];
                        double y=groceries.get(i).getcoordinate().get(j)[1];


                        TextView tv = new TextView(getContext()) ;
                        refri_icon();




                        tv.setText(groceries.get(i).getName());
                        tv.setX(150+(float)x);
                        tv.setY((float)y+60);

                        tv.setTextColor(Color.BLACK);
                        tv.setBackgroundColor(Color.GREEN);
                        preX=((float)10)*(float)i;
                        preY=(float)y;
                        refri_in.addView(tv);

                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

            }
        });


    }

    public void refri_icon(){

        iconList = new ArrayList<Icon>();

        iconList.add(new Icon(R.drawable.egg,1,"달걀"));
        iconList.add(new Icon(R.drawable.lemon,2,"레몬"));
        iconList.add(new Icon(R.drawable.plum,3,"자두"));
        iconList.add(new Icon(R.drawable.cucumber,4,"오이"));
        iconList.add(new Icon(R.drawable.cider,5,"사이다"));
        iconList.add(new Icon(R.drawable.carrot,6,"당근"));
        iconList.add(new Icon(R.drawable.squash,7,"애호박"));
        iconList.add(new Icon(R.drawable.corn,8,"옥수수"));
        iconList.add(new Icon(R.drawable.pineapple,9,"파인애플"));
        iconList.add(new Icon(R.drawable.apple,10,"사과"));
        iconList.add(new Icon(R.drawable.onion,11,"양파"));
        iconList.add(new Icon(R.drawable.garlic,12,"마늘"));
        iconList.add(new Icon(R.drawable.tomato,13,"토마토"));
        iconList.add(new Icon(R.drawable.broccoli,14,"브로콜리"));
        iconList.add(new Icon(R.drawable.sesame,15,"깻잎"));
        iconList.add(new Icon(R.drawable.eggplant,16,"가지"));
        iconList.add(new Icon(R.drawable.sweetpumpkin,17,"단호박"));
        iconList.add(new Icon(R.drawable.radish,18,"무"));
        iconList.add(new Icon(R.drawable.cabbage,19,"양배추"));
        iconList.add(new Icon(R.drawable.paprika,20,"파프리카"));
        iconList.add(new Icon(R.drawable.yakult,21,"야쿠르트"));
        iconList.add(new Icon(R.drawable.beer,22,"맥주"));
        iconList.add(new Icon(R.drawable.cola,23,"콜라"));
        iconList.add(new Icon(R.drawable.strawberry,24,"딸기"));
        
        
        
    }
}