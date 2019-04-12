package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;


public class PassWordReturnActivity extends Activity {

    private EditText et_return_name;
    private TextView tv_return_password;
    private Button bt_return_confirm;
    private Button bt_return_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        initView();

        initData();

    }

    private void initData() {
        bt_return_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  name = et_return_name.getText().toString();
                final String passWord = Model.getInstence().getUserAccountTableDao().getPassWord(name);
                tv_return_password.setText(passWord);
            }
        });
        bt_return_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        et_return_name = findViewById(R.id.et_return_name);
        tv_return_password = findViewById(R.id.tv_return_password);
        bt_return_confirm = findViewById(R.id.bt_return_confirm);
        bt_return_cancel = findViewById(R.id.bt_return_cancel);
    }
}
