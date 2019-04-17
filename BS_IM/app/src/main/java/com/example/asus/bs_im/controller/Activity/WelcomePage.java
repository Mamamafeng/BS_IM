package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WelcomePage extends Activity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //防止内存溢出，如果该Activity已经结束，不处理
            if (isFinishing()) {
                return;
            }
            //判断进入页面
            to_main0rLogin();
        }
    };
    //判断进入主页面还是登陆页面
    private void to_main0rLogin() {
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                //判断账号是否已经登陆
                if (EMClient.getInstance().isLoggedInBefore()){//已登录
                    //获取登陆信息
                    UserInfo accountInfo = Model.getInstence().getUserAccountTableDao().getAccountInfo(EMClient.getInstance().getCurrentUser());
                    if (accountInfo == null){
                        //未登录,to login页面
                        Log.d("feng","登陆信息为空");
                        Intent intent = new Intent(WelcomePage.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        // to main页面
                        Model.getInstence().loginSuccess(accountInfo);
                        Intent intent = new Intent(WelcomePage.this,MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    //未登录,to login页面
                    Log.d("feng","未登录");
                    Intent intent = new Intent(WelcomePage.this,LoginActivity.class);
                    startActivity(intent);
                }
                //结束当前页面，以便退出时一次退出程序
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        //两秒延迟，发送延迟消息
        handler.sendMessageDelayed(Message.obtain(), 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁延迟
        handler.removeCallbacksAndMessages(null);
    }

}
