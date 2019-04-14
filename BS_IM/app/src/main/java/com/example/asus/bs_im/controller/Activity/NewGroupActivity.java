package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.TokenBean;
import com.example.asus.bs_im.utils.OKHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.asus.bs_im.utils.OKHttpUtils.getToken;

public class NewGroupActivity extends Activity {
    private EditText et_newgroup_name;
    private EditText et_newgroup_context;
    private EditText et_newgroup_maxusers;
    private CheckBox cb_newgroup_isopen;//公开群/私有群属性
    private CheckBox cb_newgroup_openinvite;//开放群邀请
    private Button bt_newgroup_create;
    private boolean is_public = false;//公开群/私有群属性
    private boolean approval = true; //加入公开群是否需要批准，默认值是false
    private TokenBean tokenBean;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        initView();
        
        initListener();
    }

    private void initListener() {
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroupActivity.this,SelectContactActivity.class);
                startActivityForResult(intent,1);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getToken(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("feng",response.body().string());
                Message message = handler.obtainMessage();
                message.obj = response.body().string();
                message.sendToTarget();
            }
        });
        //成功获取
        if (resultCode == RESULT_OK){
            try {
                createGroup(data.getStringArrayExtra("contacts"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createGroup(final String[] contacts) throws JSONException {
        final String groupName = et_newgroup_name.getText().toString();
        final String groupContext = et_newgroup_context.getText().toString();
        if (cb_newgroup_isopen.isChecked()){
            is_public = true;
        }
        if (is_public == true && cb_newgroup_openinvite.isChecked()){
            approval = false;
        }
        String s = et_newgroup_maxusers.getText().toString();
        int maxusers = Integer.parseInt(s);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupname",groupName);
        jsonObject.put("desc",groupContext);
        jsonObject.put("public",is_public);
        jsonObject.put("maxusers",maxusers);
        jsonObject.put("approval",approval);
        jsonObject.put("owner",EMClient.getInstance().getCurrentUser());
        jsonObject.put("members",contacts);

        String address = "https://a1.easemob.com/1108190122085684/mahaifeng/chatgroups/" ;
        OKHttpUtils.apiCreatGroup(address, jsonObject, tokenBean.getAccess_token(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

        /*Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                *//*
                * 参数一：群名称
                * 参数二：群描述
                * 参数三：群成员
                * 参数四：建群原因
                * 参数五：参数设置
                * *//*
                EMGroupManager.EMGroupOptions optioms = new EMGroupManager.EMGroupOptions();
                optioms.maxUsers = 200;
                EMGroupManager.EMGroupStyle groupStyle = null;
                if (cb_newgroup_isopen.isChecked()){
                    //公开的
                    if (cb_newgroup_openinvite.isChecked()){
                        //开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else{
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else{
                    if (cb_newgroup_openinvite.isChecked()){
                        //开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }else{
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                optioms.style = groupStyle;
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName,groupContext,contacts,"申请加入群",optioms);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this,"创建失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });*/
    }

    private void initView() {
        et_newgroup_name = findViewById(R.id.et_newgroup_name);
        et_newgroup_context = findViewById(R.id.et_newgroup_context);
        et_newgroup_maxusers = findViewById(R.id.et_newgroup_maxusers);
        cb_newgroup_isopen = findViewById(R.id.cb_newgroup_isopen);
        cb_newgroup_openinvite = findViewById(R.id.cb_newgroup_openinvite);
        bt_newgroup_create = findViewById(R.id.bt_newgroup_create);
    }
    
}
