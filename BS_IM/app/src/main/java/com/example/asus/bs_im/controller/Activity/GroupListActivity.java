package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.adapter.GroupListAdapter;
import com.example.asus.bs_im.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupListActivity extends Activity {
    private ListView lv_group;
    private GroupListAdapter adapter;
    private LinearLayout ll_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        initView();
        initData();
        iniListener();
    }

    private void iniListener() {
        //群列表条目
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    return;
                }
                Intent intent = new Intent(GroupListActivity.this,ChatActivity.class);
                //会话类型，群会话
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                //传递群ID
                EMGroup emGroup = EMClient.getInstance().groupManager().getAllGroups().get(position - 1);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,emGroup.getGroupId());
                startActivity(intent);
            }
        });
        //新建群
        ll_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this,NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        adapter = new GroupListAdapter(this);
        lv_group.setAdapter(adapter);
        //从环信获取所有群的信息
        getGroups();
    }

    private void getGroups() {
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取群的信息
                    final List<EMGroup> groups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this,"加载群信息成功",Toast.LENGTH_SHORT).show();
                            adapter.refresh(groups);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this,"加载群信息失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        lv_group = findViewById(R.id.lv_group);
        View headview = View.inflate(this, R.layout.header_group, null);
        lv_group.addHeaderView(headview);
        ll_group = headview.findViewById(R.id.ll_group);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }
}
