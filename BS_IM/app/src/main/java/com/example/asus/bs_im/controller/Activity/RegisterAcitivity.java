package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterAcitivity extends Activity {

    private Button concel;
    private Button confirm;
    private EditText et_register_count;
    private EditText et_register_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);

        initData();
        //取消点击事件
        concel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //确定点击事件
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    //注册
    private void register() {
        final String account = et_register_count.getText().toString();
        final String password = et_register_password.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //注册账号
                    EMClient.getInstance().createAccount(account,password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Model.getInstence().getUserAccountTableDao().addAccount(new UserInfo(account,password));
                            Toast.makeText(RegisterAcitivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterAcitivity.this,"注册失败"+e.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("feng","注册失败"+e.toString());
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        concel = findViewById(R.id.concel);
        confirm = findViewById(R.id.confirm);
        et_register_count = findViewById(R.id.et_register_count);
        et_register_password = findViewById(R.id.et_register_password);
    }
}
