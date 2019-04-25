package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PassWordReturnActivity extends Activity {

    private EditText et_return_name;
    private EditText et_return_daan;
    private Button bt_return_confirm;
    private Button bt_return_cancel;
    private Spinner sp_mibaowenti;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> list;
    private boolean result;
    private String id;
    private String daan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_return);
        initView();
        initData();
        simpleAdapter = new SimpleAdapter(PassWordReturnActivity.this,datas(),R.layout.layout_spinner_item,new String[]{"text"},new int[]{R.id.tv_item});
        sp_mibaowenti.setAdapter(simpleAdapter);
        sp_mibaowenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String question = simpleAdapter.getItem(position).toString();
                //进数据库查验密保问题是否正确
                boolean question1 = getQuestion(question);
                if (!question1){
                    Toast.makeText(PassWordReturnActivity.this,"密保问题不正确",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean getQuestion(String question){
        result = false;
        //取数据库中的密保问题
        String questioninsql = "";
        if (questioninsql.equals(question)){
            result = true;
        }
        return result;
    }

    public List<Map<String, Object>> datas() {
        list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String[] strings = new String[]{"小学校名","中学校名","大学校名","最重要的人的姓名","最重要的人的生日","小学班主任的姓名","中学班主任的姓名","父亲的姓名","母亲的姓名"};
        int i = 0;
        for (i = 0; i<strings.length; i++){
            map.put("text",strings[i]);
            list.add(map);
        }
        return list;
    }

    private void initData() {
        bt_return_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                查验密保问题和答案，弹出dialog
                若正确，提示密码
                若错误，提示密保问题或答案错误
                 */
                if (result){//密保问题正确
                    String answer = "";
                    id = et_return_name.getText().toString();
                    daan = et_return_daan.getText().toString();
                    if (answer.equals(daan)){//查验答案正确，提示密码
                        Dialog dialog = new Dialog(PassWordReturnActivity.this);
                    }else {//查验答案错误，提示答案错误

                    }
                }
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
        et_return_daan = findViewById(R.id.et_return_daan);
        bt_return_confirm = findViewById(R.id.bt_return_confirm);
        bt_return_cancel = findViewById(R.id.bt_return_cancel);
        sp_mibaowenti = findViewById(R.id.sp_mibaowenti);
    }
}
