package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginActivity extends Activity {
    private EditText user_edit;
    private EditText password_edit;
    private Button bt_login;
    private Button bt_register;
    private Button bt_return_password;
    private CheckBox cb_login_displaypassword;
    private Context mContext;

    public LoginActivity() {
    }

    public LoginActivity(Context Context) {
        mContext = Context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化登登录页面控件
        initView();
        //监听登录按钮的点击事件
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        //监听注册按钮的点击事件
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterAcitivity.class);
                startActivity(intent);
            }
        });
        //监听找回密码按钮的点击事件
        bt_return_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,PassWordReturnActivity.class);
                startActivity(intent);
            }
        });
        //监听显示密码选择框的点击事件
        cb_login_displaypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_login_displaypassword.isChecked()){
                    //密码明文显示
                    password_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //密码密文显示
                    password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    private void login() {
        final String LoginAccount = user_edit.getText().toString();
        final String LoginPassword = password_edit.getText().toString();

        if (TextUtils.isEmpty(LoginAccount) || TextUtils.isEmpty(LoginPassword)){
            Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(LoginAccount, LoginPassword, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Model.getInstence().loginSuccess(new UserInfo(LoginAccount));
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        //更新用户信息到本地数据库
                        //Model.getInstence().getUserAccountTableDao().addAccount(new UserInfo(LoginAccount));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"登录失败"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }

    private void initView() {
        user_edit = findViewById(R.id.user_edit);
        password_edit = findViewById(R.id.password_edit);
        password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        bt_login = findViewById(R.id.bt_login);
        bt_register = findViewById(R.id.bt_register);
        bt_return_password = findViewById(R.id.bt_return_password);
        cb_login_displaypassword = findViewById(R.id.cb_login_displaypassword);

    }

}
