package com.example.asus.bs_im.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtils {

    public static void getToken (okhttp3.Callback callback) {
        String address = "https://a1.easemob.com/1108190122085684/mahaifeng-im/token";
        String gson = "{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA6VkZncB-aEemZ5vO6rLH8Kw\",\"client_secret\":\"YXA6rKqsDCY7gbMwKt-QJ2ZZqWWD5fs\"}";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        Request request = new Request.Builder()
                .header("content-type","application/json")
                .post(requestBody)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void rePassWord(String address, String password, String access_token, okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"newpassword "+password);
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+access_token)
                .url(address)
                .put(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void reNick(String address,String nick,String access_token,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"nickname "+nick);
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+access_token)
                .url(address)
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void registerNew(String address,String username,String password,String access_token,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String post = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), post);
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+access_token)
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addContact(String address,String access_token,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+access_token)
                .url(address)
                .post(null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void apiCreatGroup(String address, JSONObject jsonObject, String access_token, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+access_token)
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
