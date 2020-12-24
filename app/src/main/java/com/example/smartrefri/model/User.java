package com.example.smartrefri.model;

import java.io.Serializable;

public class User implements Serializable {

    //회원가입
    public String email;
    public int age;
    public int sex;
    public String phone_number;
    public String name;
    public String password;

    public String guardian_name;
    public String guardian_phone_number;

    public String purpose;

    public String mobile_useremail;
    public String mobile_username;
    public String mobile_password;

    public String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getAlarm_mode() {
        return alarm_mode;
    }

    public void setAlarm_mode(int alarm_mode) {
        this.alarm_mode = alarm_mode;
    }

    public int getOuting_mode() {
        return outing_mode;
    }

    public void setOuting_mode(int outing_mode) {
        this.outing_mode = outing_mode;
    }

    public int alarm_mode;
    public int outing_mode;
    public int motion_period;

    public boolean result;

    public User user;


    //로그인
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(User user){
        this.user = user;
    }


    //냉장고 스토리 친구 정보
    public String mobile_userImage;
    public String mobile_refriImage;


    //냉장고 스토리 리스트
    public User(String mobile_username, String mobile_userImage,String mobile_refriImage){
        this.mobile_username = mobile_username;
        this.mobile_userImage = mobile_userImage;
        this.mobile_refriImage = mobile_refriImage;

    }

    public User() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGuardian_phone_number() {
        return guardian_phone_number;
    }

    public void setGuardian_phone_number(String guardian_phone_number) {
        this.guardian_phone_number = guardian_phone_number;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    //회원가입
    public User(String email, int age, int sex, String phone_number, String name, String password, String guardian_name, String guardian_phone_number, String purpose) {
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.phone_number = phone_number;
        this.name = name;
        this.password = password;
        this.guardian_name = guardian_name;
        this.guardian_phone_number = guardian_phone_number;
        this.purpose = purpose;
    }

    //회원정보 불러오기
    public User(String email, int age, int sex, String phone_number, String name, String guardian_name, String guardian_phone_number, String purpose,String img_url,int alarm_mode,int outing_mode,int motion_period) {
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.phone_number = phone_number;
        this.name = name;
        this.guardian_name = guardian_name;
        this.guardian_phone_number = guardian_phone_number;
        this.purpose = purpose;
        this.img_url =img_url;
        this.alarm_mode = alarm_mode;
        this.outing_mode = outing_mode;
        this.motion_period = motion_period;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile_useremail() {
        return mobile_useremail;
    }

    public void setMobile_useremail(String mobile_useremail) {
        this.mobile_useremail = mobile_useremail;
    }

    public String getMobile_username() {
        return mobile_username;
    }

    public void setMobile_username(String mobile_username) {
        this.mobile_username = mobile_username;
    }

    public String getMobile_password() {
        return mobile_password;
    }

    public void setMobile_password(String mobile_password) {
        this.mobile_password = mobile_password;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getGuardian_phonenumber() {
        return guardian_phone_number;
    }

    public void setGuardian_phonenumber(String guardian_phonenumber) {
        this.guardian_phone_number = guardian_phonenumber;
    }

    public String getPurpos() {
        return purpose;
    }

    public void setPurpos(String purpos) {
        this.purpose = purpos;
    }

    public int getMotion_period() {
        return motion_period;
    }

    public void setMotion_period(int motion_period) {
        this.motion_period = motion_period;
    }

    public String getMobile_userImage() {
        return mobile_userImage;
    }

    public void setMobile_userImage(String mobile_userImage) {
        this.mobile_userImage = mobile_userImage;
    }

    public String getMobile_refriImage() {
        return mobile_refriImage;
    }

    public void setMobile_refriImage(String mobile_refriImage) {
        this.mobile_refriImage = mobile_refriImage;
    }
}