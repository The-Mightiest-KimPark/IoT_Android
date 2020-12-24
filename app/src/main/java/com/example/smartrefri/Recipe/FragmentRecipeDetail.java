package com.example.smartrefri.Recipe;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.example.smartrefri.R;
import com.example.smartrefri.application.ApplicationController;
import com.example.smartrefri.main.MainActivity;
import com.example.smartrefri.model.Recipe;
import com.example.smartrefri.network.NetworkService;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecipeDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecipeDetail extends Fragment {


    MainActivity activity;
    ImageView like_recipe_btn,share_kakao_btn,recipe_food_iv,white_heart;
    TextView recipe_food_name_tv, recipe_ingredient_tv,  recipe_howto_tv,recipe_purpose_tv;
    Boolean like;

    GestureDetector gd;
    GestureDetector .OnDoubleTapListener listener;

    Recipe recipe;

    NetworkService networkService;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRecipeDetail() {
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
     * @return A new instance of fragment FragmentRecipeDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRecipeDetail newInstance(String param1, String param2) {
        FragmentRecipeDetail fragment = new FragmentRecipeDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //레시피값전달받기
        networkService = ApplicationController.getInstance().getNetworkService();
        recipe = new Recipe();
        Bundle bundle = getArguments();
        recipe = (Recipe) bundle.getSerializable("RecipeValue");

        Log.e("레시피 값 잘 넘어왔냐고요 !!",recipe.getName());


        //서버와 통신해서 즐겨찾기 유무값 넣어주기(불린타입을 넣은이유는 시시각각 아이콘을 변경해주어야하기때문)
        like=false;

        activity.changeRecipeBtn();
        gd = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override public void onShowPress(MotionEvent e) {

            }
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }
            @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }
            @Override public void onLongPress(MotionEvent e) {

            }
            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //레시피값전달받기
        networkService = ApplicationController.getInstance().getNetworkService();

        recipe = new Recipe();
        Bundle bundle = getArguments();
        recipe = (Recipe) bundle.getSerializable("RecipeValue");

        checkLike();


        Glide.with(getContext())
                .load(recipe.getImg())
                .into(recipe_food_iv);
        recipe_food_name_tv.setText(recipe.getName());
        recipe_ingredient_tv.setText(recipe.getIngredient());
        recipe_howto_tv.setText(recipe.getHowto());
        recipe_purpose_tv.setText(recipe.getPurpose());
        Log.e("레시피 값 잘 넘어왔냐고요 !22222!",recipe.getName());

        activity.changeRecipeBtn();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        like_recipe_btn = view.findViewById(R.id.like_recipe_btn);
        share_kakao_btn = view.findViewById(R.id.share_recipe_btn);
        recipe_food_iv = view.findViewById(R.id.recipe_food_iv);

        recipe_food_name_tv = view.findViewById(R.id.recipe_food_name_tv);
        recipe_ingredient_tv = view.findViewById(R.id.recipe_food_ingredient_tv);
        recipe_howto_tv = view.findViewById(R.id.recipe_howto_tv);
        recipe_purpose_tv = view.findViewById(R.id.recipe_purpose_tv);

        white_heart = view.findViewById(R.id.white_heart);
        white_heart.setVisibility(View.INVISIBLE);

        share_kakao_btn = view.findViewById(R.id.share_recipe_btn);

        Glide.with(getContext())
                .load(recipe.getImg())
                .into(recipe_food_iv);
        recipe_food_name_tv.setText(recipe.getName());
        recipe_ingredient_tv.setText(recipe.getIngredient());
        recipe_howto_tv.setText(recipe.getHowto());



        share_kakao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakaolink();
            }
        });

        listener = new GestureDetector.OnDoubleTapListener() {
            @Override public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                if(like ==false) {
                    //하트 애니메이션 구현
                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
                    white_heart.startAnimation(animation);
                    white_heart.setVisibility(View.INVISIBLE);
                    //서버와 통신 구현 부분
                    likeRecipe();

                }else if(like == true){
//                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
//                    white_heart.startAnimation(animation);
//                    white_heart.setVisibility(View.INVISIBLE);
                    likeRecipe();
                }
                return true;
            }

        };

        gd.setOnDoubleTapListener(listener);

        recipe_food_iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(gd != null){
                    gd.onTouchEvent(motionEvent);
                    return true;
                }
                return false;
            }
        });



        like_recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeRecipe();

            }
        });



        return view;

    }

    public void kakaolink() {
        FeedTemplate params = FeedTemplate.
                        newBuilder(ContentObject.newBuilder("오늘 식사 메뉴 이거 어때요?",
                        recipe.getImg(),
                        LinkObject.newBuilder().setWebUrl("https://www.10000recipe.com/recipe/"+String.valueOf(recipe.getRecipe_num()))
                                .setMobileWebUrl("https://www.10000recipe.com/recipe/6940325").build())
                        .setDescrption(recipe.getName()+"\n"+recipe.getIngredient())
                        .build())

                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(getContext(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }

    public void likeRecipe(){
        Call<Void> putLikeRecipe = networkService.putLikeRecipe(recipe);
        putLikeRecipe.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.e("즐찾 성공했나요?",String.valueOf(response.code()));
                Log.e("즐찾 성공했나요?",response.message());
                if(like==true){
                    offLike();
                    like = false;
                }else{
                    onLike();
                    like = true;
                }




            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void onLike(){
        //즐겨찾기한 레시피가 아닐때
        like_recipe_btn.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
        like_recipe_btn.setImageResource(R.drawable.heart);
        //서버와 통신 구현 부분

    }
    public void offLike(){
        //즐겨찾기한 레시피일때
        like_recipe_btn.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        like_recipe_btn.setImageResource(R.drawable.emptyheart);
    }




    public void checkLike(){
        Call<ArrayList<Recipe>> getLikeRecipeList = networkService.getLikeRecipeList(ApplicationController.user_id);
        getLikeRecipeList.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> likeRecipeList = new ArrayList<>();
                likeRecipeList = response.body();

                for(int i = 0; i<likeRecipeList.size();i++){
                    if(likeRecipeList.get(i).getAll_recipe_id() == recipe.getAll_recipe_id()){
                        like=true;
                        onLike();
                    }
                }
                Log.e("즐겨찾기 레시피 잘 불러졌나요?",String.valueOf(response.code()));



            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });

    }
}