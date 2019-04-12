package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.bean.TokenBean;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.asus.bs_im.utils.OKHttpUtils.getToken;
import static com.example.asus.bs_im.utils.OKHttpUtils.rePassWord;

public class RePassWordActivity extends Activity {

    private EditText et_modify_name;
    private Button bt_modify_cancel;
    private Button bt_modify_modify;
    private String newPassWord;
    private Gson gson;
    private TokenBean tokenBean;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_pass_word);
        initView();
        initListener();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null){
                String result =  msg.obj.toString();
                tokenBean = gson.fromJson(result,TokenBean.class);
            }
        }
    };

    private void initListener() {
        newPassWord = String.valueOf(et_modify_name.getText());
        user = EMClient.getInstance().getCurrentUser();
        String url = "https://a1.easemob.com/1108190122085684/mahaifeng-im/token";
        String gson = "{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA6VkZncB-aEemZ5vO6rLH8Kw\",\"client_secret\":\"YXA6rKqsDCY7gbMwKt-QJ2ZZqWWD5fs\"}";
        getToken(url, gson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                message.obj = response.body().string();
                message.sendToTarget();
            }
        });

        bt_modify_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseUrl = "https://a1.easemob.com/1108190122085684/mahaifeng/users/" +
                        user+ "/password";
                try {
                    rePassWord(baseUrl, newPassWord, tokenBean.getAccess_token(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RePassWordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d("feng",response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(RePassWordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initView() {
        et_modify_name = findViewById(R.id.et_modify_name);
        bt_modify_cancel = findViewById(R.id.bt_modify_cancel);
        bt_modify_modify = findViewById(R.id.bt_modify_modify);
    }
}
