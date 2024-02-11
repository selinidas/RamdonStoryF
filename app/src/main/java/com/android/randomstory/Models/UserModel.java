package com.android.randomstory.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable {

    String name, email, password, gender, userId, imageUrl, token, visibility;
    ArrayList<PostModel> favList;

    public UserModel(String name, String email, String password, String gender, String userId, String imageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public UserModel(String name, String email, String password, String gender, String userId, String imageUrl, String token, String visibility, ArrayList<PostModel> favList) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.token = token;
        this.visibility = visibility;
        this.favList = favList;
    }

    public ArrayList<PostModel> getFavList() {
        return favList;
    }

    public void setFavList(ArrayList<PostModel> favList) {
        this.favList = favList;
    }

    public UserModel(String name, String email, String password, String gender, String userId, String imageUrl, String visibility) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.visibility = visibility;
    }


    public UserModel() {
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", userId='" + userId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
