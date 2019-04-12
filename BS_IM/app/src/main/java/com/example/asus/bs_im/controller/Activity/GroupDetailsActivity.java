package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.adapter.GroupDetailAdapter;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailsActivity extends Activity {
    private GridView gv_group_detail;
    private Button bt_exit_group;
    private EMGroup group;
    private GroupDetailAdapter.OnGroupDetailListener onGroupDetailListener = new GroupDetailAdapter.OnGroupDetailListener() {
        @Override
        public void onAddMembers() {
            Intent intent = new Intent(GroupDetailsActivity.this,SelectContactActivity.class);
            intent.putExtra(Constant.GROUP_ID,group.getGroupId());
            startActivityForResult(intent,2);
        }

        @Override
        public void onDeleteMembers(final UserInfo userInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //从环信服务器删除此人
                        EMClient.getInstance().groupManager().removeUserFromGroup(group.getGroupId(),userInfo.getUserId());
                        //更新页面
//                        getMembers();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getMembers();
                                Toast.makeText(GroupDetailsActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailsActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //获取邀请的群成员信息
            final String[] contacts = data.getStringArrayExtra("contacts");
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //从环信发送邀请信息
                        EMClient.getInstance().groupManager().addUsersToGroup(group.getGroupId(),contacts);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailsActivity.this,"发送邀请成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailsActivity.this,"发送邀请失败"+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    private List<UserInfo> userInfos;
    private GroupDetailAdapter groupDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        initView();

        getData();

        initData();

        getMembers();

        initListener();
    }

    private void initListener() {
        gv_group_detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //判断当前是否是删除模式
                        if (groupDetailAdapter.isDeleteMode()){
                            //切换为非删除模式
                            groupDetailAdapter.setDeleteMode(false);
                            //刷新页面
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void getMembers() {
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //从环信服务器获取所有的群成员信息
                    EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(group.getGroupId());
                    List<String> members = groupFromServer.getMembers();
                    if (members != null && members.size() >= 0){
                        userInfos = new ArrayList<>();
                        for (String mem : members){
                            UserInfo userInfo = new UserInfo(mem);
                            userInfos.add(userInfo);
                        }
                    }
                    //更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupDetailAdapter.refreshUser(userInfos);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailsActivity.this,"获取群成员信息失败"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        initButtonDisplay();
        initGridView();
    }

    private void initGridView() {
        //当前用户是群主或者开放群邀请
        boolean isOwner= EMClient.getInstance().getCurrentUser().equals(group.getOwner()) || group.isPublic();
        groupDetailAdapter = new GroupDetailAdapter(this, isOwner,onGroupDetailListener);
        gv_group_detail.setAdapter(groupDetailAdapter);
    }

    private void initButtonDisplay() {
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())){
            bt_exit_group.setText("解散群");
            bt_exit_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstence().getallthreadpool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(group.getGroupId());
                                //发送群解散的广播
                                exitGroupBroadCast();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailsActivity.this,"解散群成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailsActivity.this,"解散群失败"+e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }else{
            bt_exit_group.setText("退出群");
            bt_exit_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstence().getallthreadpool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(group.getGroupId());
                                //发送退出群的广播
                                exitGroupBroadCast();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailsActivity.this,"退出群成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailsActivity.this,"退出群失败"+e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
    //解散群和退群广播
    private void exitGroupBroadCast() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(GroupDetailsActivity.this);
        Intent intent = new Intent(Constant.EXIT_GROUP);
        intent.putExtra(Constant.GROUP_ID,group.getGroupId());
        localBroadcastManager.sendBroadcast(intent);
    }

    private void getData() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);
        if (groupId == null){
            return;
        }else{
            group = EMClient.getInstance().groupManager().getGroup(groupId);

        }
    }

    private void initView() {
        gv_group_detail = findViewById(R.id.gv_group_detail);
        bt_exit_group = findViewById(R.id.bt_exit_group);
    }
}
