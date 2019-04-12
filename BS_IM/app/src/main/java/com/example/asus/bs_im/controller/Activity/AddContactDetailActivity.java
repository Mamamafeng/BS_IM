package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddContactDetailActivity extends Activity {

    private ImageView iv_contact_detail_logo;
    private TextView tv_contact_detail_id;
    private TextView tv_contact_detail_nick;
    private EditText et_contact_detail_reason;
    private Button bt_contact_detail_cancel;
    private Button bt_contact_detail_add;
    private UserInfo accountInfo;
    private String reasonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_detail);
        
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        bt_contact_detail_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_contact_detail_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
    }

    private void addContact() {
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(accountInfo.getUserId(),reasonText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactDetailActivity.this,"发送添加好友成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactDetailActivity.this,"发送添加好友失败"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        String id = getIntent().getStringExtra("id");
        accountInfo = Model.getInstence().getUserAccountTableDao().getAccountInfo(id);
        String nick = accountInfo.getNick();
        byte[] picture = accountInfo.getPicture();
        Drawable picture1 = SpUtils.getInstance().picture(picture);
        iv_contact_detail_logo.setImageDrawable(picture1);
        tv_contact_detail_id.setText(id);
        tv_contact_detail_nick.setText(nick);
        reasonText = String.valueOf(et_contact_detail_reason.getText());
    }

    private void initView() {
        iv_contact_detail_logo = findViewById(R.id.iv_contact_detail_logo);
        tv_contact_detail_id = findViewById(R.id.tv_contact_detail_id);
        tv_contact_detail_nick = findViewById(R.id.tv_contact_detail_nick);
        et_contact_detail_reason = findViewById(R.id.et_contact_detail_reason);
        bt_contact_detail_cancel = findViewById(R.id.bt_contact_detail_cancel);
        bt_contact_detail_add = findViewById(R.id.bt_contact_detail_add);
    }
}
