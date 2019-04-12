package com.example.asus.bs_im.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtils {

    public static void getToken (String address, String gson, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        Request request = new Request.Builder()
                .header("content-type","application/json")
                .post(requestBody)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void rePassWord(String address, String password, String access_token, okhttp3.Callback callback) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"newpassword "+password);
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+access_token)
                .url(address)
                .put(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
