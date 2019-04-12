package com.example.asus.bs_im.controller.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.Activity.LoginActivity;
import com.example.asus.bs_im.controller.Activity.ReNickActivity;
import com.example.asus.bs_im.controller.Activity.SetPictureActivity;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.utils.Constant;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class SettingFragment extends Fragment {
    private Button main_setting_logout;
    private Button setting_reNick;
    private Button bt_setting_repassword;
    private ImageView iv_setting_avatar;
    private TextView tv_setting_nick;
    private TextView tv_setting_name;
    private String currentUser;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver userInfoChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshView();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.setting_fragment, null);
        initView(view);
        return view;

    }

    private void initView(View view) {
        main_setting_logout = view.findViewById(R.id.main_setting_logout);
        setting_reNick = view.findViewById(R.id.setting_reNick);
        bt_setting_repassword = view.findViewById(R.id.bt_setting_repassword);
        iv_setting_avatar = view.findViewById(R.id.iv_setting_avatar);
        tv_setting_nick = view.findViewById(R.id.tv_setting_nick);
        tv_setting_name = view.findViewById(R.id.tv_setting_name);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestWritePermission();
        initData();
    }

    private void initData() {
        //设置显示账号
        currentUser = EMClient.getInstance().getCurrentUser();
        tv_setting_name.setText(currentUser);

        //设置一个广播接收器接收昵称和头像的变化
        localBroadcastManager.registerReceiver(userInfoChangedReceiver,new IntentFilter(Constant.PHOTO_NICK_CHANGED));

        //设置显示昵称
        tv_setting_nick.setText(Model.getInstence().getUserAccountTableDao().getAccountInfo(currentUser).getNick());

        //设置显示头像
        byte[] p = Model.getInstence().getUserAccountTableDao().getPictureFromSql(currentUser);
        if (p != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(p,0,p.length,null);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            Drawable drawable = bitmapDrawable;
            iv_setting_avatar.setImageDrawable(drawable);
        }else{
            iv_setting_avatar.setImageResource(R.drawable.ease_default_avatar);
        }

        //设置ImageView点击事件——点击选择图片
        iv_setting_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SetPictureActivity.class);
                intent.putExtra(Constant.USER_ID,EMClient.getInstance().getCurrentUser());
                startActivity(intent);
            }
        });

        //设置修改昵称的点击事件
        setting_reNick.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ReNickActivity.class);
                intent.putExtra(Constant.USER_ID,currentUser);
                startActivity(intent);
            }
        });

        bt_setting_repassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RePassWordActivity.class);
                intent.putExtra(Constant.USER_ID,currentUser);
                startActivity(intent);
            }
        });


        main_setting_logout.setText("退出("+EMClient.getInstance().getCurrentUser()+")的登录");
        main_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //联网操作，开启新的线程
                Model.getInstence().getallthreadpool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器 执行退出操作
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //关闭数据库
                                Model.getInstence().getdBmanager().close();
                                //回到主页面并更新UI，当前位于联网子线程，需回到主线程
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                                        //返回主页面
                                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出失败"+s,Toast.LENGTH_SHORT).show();
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
        });
    }

    public void refreshView(){
        iv_setting_avatar.setImageDrawable(SpUtils.getInstance().picture(Model.getInstence().getUserAccountTableDao().getPictureFromSql(currentUser)));
        tv_setting_nick.setText(Model.getInstence().getUserAccountTableDao().getAccountInfo(currentUser).getNick());
        tv_setting_name.setText(currentUser);
    }

    private void requestWritePermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
}
