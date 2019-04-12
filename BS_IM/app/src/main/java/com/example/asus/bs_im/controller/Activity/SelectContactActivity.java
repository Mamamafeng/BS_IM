package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.adapter.SelectContactsAdapter;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.SelectContactInfo;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class SelectContactActivity extends Activity {
    private TextView tv_select_save;
    private ListView lv_select_item;
    private List<SelectContactInfo> selects;
    private SelectContactsAdapter selectContactsAdapter;
    private List<String> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        getDataFromGroupDetails();
        initView();
        initData();
        initListener();
    }

    private void getDataFromGroupDetails() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);
        if (groupId != null){
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            //获取群中已经存在的群成员
            members = group.getMembers();
        }
        if (members == null){
            members = new ArrayList<>();
        }
    }

    private void initListener() {
        lv_select_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb_selected = view.findViewById(R.id.cb_selected);
                cb_selected.setChecked(!cb_selected.isChecked());
                //修改Bean
                SelectContactInfo selectContactInfo = selects.get(position);
                selectContactInfo.setChecked(cb_selected.isChecked());
                //刷新页面;
                selectContactsAdapter.notifyDataSetChanged();
            }
        });
        //处理保存按钮的点击事件
        tv_select_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取已经选择的联系人
                List<String> ids = selectContactsAdapter.getSelectContacts();
                //给启动页面返回数据
                Intent intent = new Intent();
                intent.putExtra("contacts",ids.toArray(new String[0]));
                setResult(RESULT_OK,intent);

                finish();
            }
        });

    }

    private void initData() {
        //从本地数据库中获取所有联系人
        List<UserInfo> contacts = Model.getInstence().getdBmanager().getContactTableDao().getContacts();
        selects = new ArrayList<>();
        if (contacts != null && contacts.size() >= 0){
            //转换为SelectContactInfo
            for (UserInfo users : contacts){
                SelectContactInfo selectContactInfo = new SelectContactInfo(users, false);
                selects.add(selectContactInfo);
            }
        }
        //加载适配器
        selectContactsAdapter = new SelectContactsAdapter(this,selects,members);
        lv_select_item.setAdapter(selectContactsAdapter);
    }

    private void initView() {
        tv_select_save = findViewById(R.id.tv_select_save);
        lv_select_item = findViewById(R.id.lv_select_item);
    }
}
