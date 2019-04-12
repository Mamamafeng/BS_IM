package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.fragment.ChatFragment;
import com.example.asus.bs_im.controller.fragment.ContactFragment;
import com.example.asus.bs_im.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

    private RadioGroup radioGroup;
    private ChatFragment chatFragment;
    private ContactFragment contactFragment;
    private SettingFragment settingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initData();

        initListener();

    }

    private void initListener() {
        //RadioGroup的监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId){
                    //会话页面
                    case R.id.main_chat:
                        fragment = chatFragment;
                        break;
                    //联系人页面
                    case R.id.main_contact:
                        fragment = contactFragment;
                        break;
                     //设置页面
                    case R.id.main_setting:
                        fragment = settingFragment;
                        break;
                }
                //实现fragment切换
                changeFragment(fragment);
            }
        });
        //默认选择会话页面
        radioGroup.check(R.id.main_chat);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_layout,fragment).commit();
    }


    //初始化三个Fragment
    private void initData() {
        chatFragment = new ChatFragment();
        contactFragment = new ContactFragment();
        settingFragment = new SettingFragment();
    }

    private void init() {
        radioGroup = findViewById(R.id.main_bottom_radiogroup);
    }
}
