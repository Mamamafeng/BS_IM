package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

public class NewGroupActivity extends Activity {
    private EditText et_newgroup_name;
    private EditText et_newgroup_context;
    private CheckBox cb_newgroup_isopen;
    private CheckBox cb_newgroup_openinvite;
    private Button bt_newgroup_create;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //成功获取
        if (resultCode == RESULT_OK){
            createGroup(data.getStringArrayExtra("contacts"));
        }
    }

    private void createGroup(final String[] contacts) {
        final String groupName = et_newgroup_name.getText().toString();
        final String groupContext = et_newgroup_context.getText().toString();

        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                /*
                * 参数一：群名称
                * 参数二：群描述
                * 参数三：群成员
                * 参数四：建群原因
                * 参数五：参数设置
                * */
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
        });
    }

    private void initView() {
        et_newgroup_name = findViewById(R.id.et_newgroup_name);
        et_newgroup_context = findViewById(R.id.et_newgroup_context);
        cb_newgroup_isopen = findViewById(R.id.cb_newgroup_isopen);
        cb_newgroup_openinvite = findViewById(R.id.cb_newgroup_openinvite);
        bt_newgroup_create = findViewById(R.id.bt_newgroup_create);
    }
    
}
