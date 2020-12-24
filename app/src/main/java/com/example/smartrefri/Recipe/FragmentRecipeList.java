package com.example.smartrefri.Recipe;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.main.StoryAdapter;
import com.example.smartrefri.model.Like;
import com.example.smartrefri.model.Recipe;
import com.example.smartrefri.model.User;
import com.example.smartrefri.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecipeList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecipeList extends Fragment {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    LinearLayoutManager recipeLayoutManager;
    Fragment recipe_detail = new FragmentRecipeDetail();
    FragmentTransaction transaction;

    LinearLayout recipe_list_item;
    ArrayList<Recipe> recipeList;
    ArrayList<Like> likeList;

    TextView recipe_list_title_tv;

    Bundle bundle;

    NetworkService networkService;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MainActivity activity;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRecipeList() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)getActivity();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     *
     * @return A new instance of fragment FragmentRecipeList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRecipeList newInstance(String param1, String param2) {
        FragmentRecipeList fragment = new FragmentRecipeList();
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
        bundle = getArguments();
        networkService = ApplicationController.getInstance().getNetworkService();
        activity.changeRecipeBtn();


//        즐겨찾기리스트,오늘의레시피전체리스트

//        activity.changeRecipeBtn();

//        recipeList = new ArrayList<Recipe>();
//        recipeList.add(new Recipe(1, "김치볶음밥","김치,양파, 쌀, 식용유, 햄, 참기름, 김가루","간장,고추장,후추","1.재료의 양은 입맛에 맞게 취향껏 준비해주세요.\n2.먼저 묵은지를 가위를 사용해서 잘게 썰어주세요.\n 3.만약 신김치가 없으면 덜익은 김치에 식초 2T를 넣어주세요.\n4.대파도 송송 썰어 주세요.\n 5.후라이팬에 식용유를 듬뿍 넣은 뒤 충분히 달군 다음 참치를 넣고 볶아주세요.\n6. 참치의 비린 맛을 잡기 위해 간 마늘 1T를 넣고 볶아주세요.\n7.어느 정도 볶은 다음 송송 썬 대파를 넣고 계속 볶아주세요.\n",R.drawable.kimchi));
//
//        recipeList.add(new Recipe(1, "참치김치볶음밥","김치,양파, 쌀, 식용유, 햄, 참기름, 김가루","간장,고추장,후추","1.재료의 양은 입맛에 맞게 취향껏 준비해주세요.\n2.먼저 묵은지를 가위를 사용해서 잘게 썰어주세요.\n 3.만약 신김치가 없으면 덜익은 김치에 식초 2T를 넣어주세요.\n4.대파도 송송 썰어 주세요.\n 5.후라이팬에 식용유를 듬뿍 넣은 뒤 충분히 달군 다음 참치를 넣고 볶아주세요.\n6. 참치의 비린 맛을 잡기 위해 간 마늘 1T를 넣고 볶아주세요.\n7.어느 정도 볶은 다음 송송 썬 대파를 넣고 계속 볶아주세요.\n",R.drawable.kimchi));
//
//        recipeList.add(new Recipe(1, "비빔밥","김치,양파, 쌀, 식용유, 햄, 참기름, 김가루","간장,고추장,후추","1.재료의 양은 입맛에 맞게 취향껏 준비해주세요.\n2.먼저 묵은지를 가위를 사용해서 잘게 썰어주세요.\n 3.만약 신김치가 없으면 덜익은 김치에 식초 2T를 넣어주세요.\n4.대파도 송송 썰어 주세요.\n 5.후라이팬에 식용유를 듬뿍 넣은 뒤 충분히 달군 다음 참치를 넣고 볶아주세요.\n6. 참치의 비린 맛을 잡기 위해 간 마늘 1T를 넣고 볶아주세요.\n7.어느 정도 볶은 다음 송송 썬 대파를 넣고 계속 볶아주세요.\n",R.drawable.kimchi));
//
//        recipeList.add(new Recipe(1, "고추장볶음밥","김치,양파, 쌀, 식용유, 햄, 참기름, 김가루","간장,고추장,후추","1.재료의 양은 입맛에 맞게 취향껏 준비해주세요.\n2.먼저 묵은지를 가위를 사용해서 잘게 썰어주세요.\n 3.만약 신김치가 없으면 덜익은 김치에 식초 2T를 넣어주세요.\n4.대파도 송송 썰어 주세요.\n 5.후라이팬에 식용유를 듬뿍 넣은 뒤 충분히 달군 다음 참치를 넣고 볶아주세요.\n6. 참치의 비린 맛을 잡기 위해 간 마늘 1T를 넣고 볶아주세요.\n7.어느 정도 볶은 다음 송송 썬 대파를 넣고 계속 볶아주세요.\n",R.drawable.kimchi));
//
//        recipeList.add(new Recipe(1, "김치장조림볶음밥","김치,양파, 쌀, 식용유, 햄, 참기름, 김가루","간장,고추장,후추","1.재료의 양은 입맛에 맞게 취향껏 준비해주세요.\n2.먼저 묵은지를 가위를 사용해서 잘게 썰어주세요.\n 3.만약 신김치가 없으면 덜익은 김치에 식초 2T를 넣어주세요.\n4.대파도 송송 썰어 주세요.\n 5.후라이팬에 식용유를 듬뿍 넣은 뒤 충분히 달군 다음 참치를 넣고 볶아주세요.\n6. 참치의 비린 맛을 잡기 위해 간 마늘 1T를 넣고 볶아주세요.\n7.어느 정도 볶은 다음 송송 썬 대파를 넣고 계속 볶아주세요.\n",R.drawable.kimchi));
    }
    @Override
    public void onResume() {
        super.onResume();
        activity.changeRecipeBtn();
        bundle = getArguments();
        if(bundle.getString("listName").equals("bookmark")){
            recipe_list_title_tv.setText("즐겨찾기 레시피 목록");
            getLikeRecipeList();
            Log.e("리스트이름","즐겨찾기레시피입니다");
        }else{
            recipe_list_title_tv.setText("오늘의 추천 레시피 목록");
            getRecipeList();
            Log.e("리스트이름","추천레시피입니다");
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recipeRecyclerView = view.findViewById(R.id.recipe_listview);
        recipe_list_item = view.findViewById(R.id.recipe_list_item);
        recipeLayoutManager = new LinearLayoutManager(getContext());
        recipeLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recipeRecyclerView.setLayoutManager(recipeLayoutManager);
        recipe_list_title_tv = view.findViewById(R.id.recipe_list_title_tv);







        return view;
    }
//    public void goToDetail(){
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("RecipeValue",recipe);
//        recipe_detail.setArguments(bundle);
//        transaction.replace(R.id.frameLayout,recipe_detail);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    public void getLikeRecipeList(){
        Call<ArrayList<Recipe>> getLikeRecipeList = networkService.getLikeRecipeList(ApplicationController.user_id);
        getLikeRecipeList.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> likeRecipeList = new ArrayList<>();
                likeRecipeList = response.body();
//                activity.changeRecipeBtn();
                recipeAdapter = new RecipeAdapter(getContext(),likeRecipeList,getFragmentManager());
                recipeRecyclerView.setAdapter(recipeAdapter);
                Log.e("즐겨찾기 레시피 잘 불러졌나요?",String.valueOf(response.code()));



            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });

    }
    public void getRecipeList(){
        Log.e("추천 레시피 잘 불러졌나요?","왜요?");
        Call<ArrayList<Recipe>> getRecommendRecipeList = networkService.getRecommendRecipeList(ApplicationController.user_id);
        getRecommendRecipeList.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recommendList = new ArrayList<Recipe>();
                recommendList = response.body();
//                activity.changeRecipeBtn();
                recipeAdapter = new RecipeAdapter(getContext(),recommendList,getFragmentManager());
                recipeRecyclerView.setAdapter(recipeAdapter);
                Log.e("추천 레시피 잘 불러졌나요?",String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e("혹시뭐가안된요?",t.getMessage());

            }
        });

    }

}