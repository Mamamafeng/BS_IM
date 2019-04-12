package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddContactActivity extends Activity {
    private TextView tv_add_search;
    private EditText et_add_name;
    private RelativeLayout rl_add;
    private TextView tv_add_name;
    private ImageView add_iv;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initView();
        tv_add_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void search() {
        final String name = et_add_name.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        userInfo = Model.getInstence().getUserAccountTableDao().getAccountInfo(name);
        if(userInfo != null){
            rl_add.setVisibility(View.VISIBLE);
            if(userInfo.getNick() != null){
                tv_add_name.setText(userInfo.getNick());
            }else{
                tv_add_name.setText(userInfo.getName());
            }
            add_iv.setImageDrawable(SpUtils.getInstance().picture(Model.getInstence().getUserAccountTableDao().getPictureFromSql(userInfo.getUserId())));
            rl_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddContactActivity.this,AddContactDetailActivity.class);
                    intent.putExtra("id",name);
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(AddContactActivity.this,"用户不存在！",Toast.LENGTH_SHORT).show();
        }
    }



    private void initView() {
        add_iv = findViewById(R.id.add_iv);
        tv_add_search = findViewById(R.id.tv_add_search);
        et_add_name = findViewById(R.id.et_add_name);
        rl_add = findViewById(R.id.rl_add);
        tv_add_name = findViewById(R.id.tv_add_name);
    }
}
