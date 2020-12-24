package com.example.smartrefri.network;

import com.example.smartrefri.R;
import com.example.smartrefri.model.Alarm;
import com.example.smartrefri.model.AlarmMode;
import com.example.smartrefri.model.All_Grocery;
import com.example.smartrefri.model.Dvsensor;
import com.example.smartrefri.model.Follow;
import com.example.smartrefri.model.Fridge;
import com.example.smartrefri.model.Grocery;
import com.example.smartrefri.model.Mode;
import com.example.smartrefri.model.Recipe;
import com.example.smartrefri.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

//    GET

    @GET("/api/recomm-recipe-one")
    Call<Recipe> getMainRecommendRecipe(@Query("email") String email);

    @GET("/api/follow-userinfo/")
    Call<User> getUserInfo(@Query("email") String email);

    @PUT("/api/going-out-mode/")
    Call<Void> changeOuting(@Body Mode mode);

    //센서값 불러오기
    @GET("/api/sensorvalue")
    Call<ArrayList<Dvsensor>> getSensorValue(@Query("email") String email, @Query("name") String name);

    //전체 식료품 값(검색창 값)
    @GET("/api/all-grocery-name/")
    Call<ArrayList<All_Grocery>> getALLGroceryList();

    //식재료 추가
    @POST("/api/user-input-grocery/")
    Call<Void> addGrocery(@Body Grocery grocery);

    //식재료 수정
    @PUT("/api/user-input-grocery/")
    Call<Void> updateGrocery(@Body Grocery grocery);

    //식재료 삭제
    @DELETE("/api/user-input-grocery/")
    Call<Void> deleteGrocery(@Query("email") String email, @Query("all_grocery_id") int all_grocery_id);

    //식재료 삭제2
//    @HTTP(method = "DELETE",path = "api/user-input-grocery",hasBody = true)
//    Call<Void> deleteGrocery(@Body Grocery grocery);

    //식재료 알림 설정 추가
    @POST("/api/grocery-alarm/")
    Call<Void> addGroceryAlarm(@Body Alarm alarm);

    //식재료 알림 수정
    @PUT("/api/grocery-alarm/")
    Call<Void> updateGroceryAlarm(@Body Alarm alarm);

    //식재료 알림 삭제
    @DELETE("/api/grocery-alarm/")
    Call<Void> deleteGroceryAlarm(@Query("email") String email, @Query("all_grocery_id") int all_grocery_id);


    //사용자별 식재료 알림 조회
    @GET("/api/grocery-alarm")
    Call<ArrayList<Alarm>> getGroceryAlarmList(@Query("email") String email);
    

    //스마트칸,커스텀 따로 식재료 불러오기(구분값 : 1(이미지), 2(직접 입력))
    @GET("/api/user-input-grocery")
    Call<ArrayList<Grocery>> getGrocryList(@Query("gubun") int gubun, @Query("email") String email);


    //전체 식재료 정보 불러오기(1과 2구분없이) 알람 식재료와 연관
    @GET("/api/user-input-grocery")
    Call<ArrayList<Grocery>> getGroceryListALL(@Query("email") String email);



    //알림 ONOFF변경
    @PUT("api/alarm-mode/")
    Call<Void> putAlarmMode(@Body AlarmMode alarmMode);

    //팔로우,언팔로우
    @PUT("/api/follow/")
    Call<Void> putFollow(@Body Follow follow);

    //스토리 사진 읽음 표시
    @PUT("/api/follow-read/")
    Call<Void> putStoryRead(@Body Follow follow);

    //팔로우 리스트 불러오기
    @GET("/api/follow-latest-photo/")
    Call<ArrayList<Follow>> getFollowList(@Query("email") String email);

    //회원가입과 로그인 후에 냉장고 정보 없으면 냉장고 번호 등록
    @PUT("api/insert-email-to-fridge/")
    Call<Void> putFridge(@Body Fridge fridge);

    @GET("/api/register-check/")
    Call<User> checkFridge(@Query("emails") String email);

    //회원가입
    @POST("/api/sign-up/")
    Call<Void> signUpUser(@Body User user);

    //로그인
    @POST("/api/sign-in/")
    Call<Void> signInUser(@Body User user);

    //레시피 즐겨찾기 등록/취소
    @PUT("api/recipe-favorite/")
    Call<Void> putLikeRecipe(@Body Recipe recipe);

    //즐겨찾기한 레시피 리스트 조회
    @GET("api/recipe-favorite/")
    Call<ArrayList<Recipe>> getLikeRecipeList(@Query("email") String email);

    //추천 레시피 리스트 조회
    @GET("api/recomm-recipe/")
    Call<ArrayList<Recipe>> getRecommendRecipeList(@Query("email") String email);




}
