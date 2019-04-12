package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.utils.Constant;
import com.hyphenate.chat.EMClient;

public class ReNickActivity extends Activity {

    private EditText et_renick_nick;
    private Button bt_renick;
    private String nick;
    private String userId;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_nick);

        initView();

        initData();

        bt_renick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reNick();
            }
        });
    }

    private void initData() {
        userId = getIntent().getStringExtra(Constant.USER_ID);
    }

    private void reNick() {
        //获取所设置的昵称
        nick = et_renick_nick.getText().toString();
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                nickChanged();
                EMClient.getInstance().updateCurrentUserNick(nick);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Model.getInstence().getUserAccountTableDao().reNick(userId,nick);
                        nickChanged();
                        Toast.makeText(ReNickActivity.this,"昵称修改成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        finish();
    }

    private void nickChanged(){
        localBroadcastManager = LocalBroadcastManager.getInstance(ReNickActivity.this);
        Intent intent = new Intent(Constant.PHOTO_NICK_CHANGED);
        intent.putExtra(Constant.USER_ID,userId);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void initView() {
        et_renick_nick = findViewById(R.id.et_renick_nick);
        bt_renick = findViewById(R.id.bt_renick);
    }
}
