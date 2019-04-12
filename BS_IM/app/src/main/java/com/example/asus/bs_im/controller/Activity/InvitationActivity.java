package com.example.asus.bs_im.controller.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.adapter.InviteAdapter;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.InvitationInfo;
import com.example.asus.bs_im.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class InvitationActivity extends Activity {


    private ListView lv_invitation;
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager localBroadcastManager;
    private InviteAdapter.OnClickListener onClickListener = new InviteAdapter.OnClickListener() {
        @Override
        public void onAccept(final InvitationInfo invitationInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invitationInfo.getUserInfo().getUserId());
                        //更新数据库
                        Model.getInstence().getdBmanager().getInvitationTableDao().updateInvitationStatus(InvitationInfo.InvitationStatusn.INVITATION_ACCEPT,invitationInfo.getUserInfo().getUserId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"接受邀请成功",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onReject(final InvitationInfo invitationInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invitationInfo.getUserInfo().getUserId());
                        //数据库更新
                        Model.getInstence().getdBmanager().getInvitationTableDao().removeInvitation(invitationInfo.getUserInfo().getUserId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"拒绝邀请成功",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"拒绝邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onGroupInvitationAccept(final InvitationInfo invitationInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptInvitation(invitationInfo.getGroupInfo().getGroupId(),invitationInfo.getGroupInfo().getGroup_invitePerson());
                        invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.ACCEPTE_GROUP_INVITATION);
                        Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"接收邀请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"接收邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onGroupInvitationReject(final InvitationInfo invitationInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineInvitation(invitationInfo.getGroupInfo().getGroupId(),invitationInfo.getGroupInfo().getGroup_invitePerson(),"拒绝邀请");
                        invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.REJECT_GROUP_INVITATION);
                        Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"拒绝邀请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"拒绝邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onApplicationAccept(final InvitationInfo invitationInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptApplication(invitationInfo.getGroupInfo().getGroupId(),invitationInfo.getGroupInfo().getGroup_invitePerson());
                        invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.ACCEPTE_GROUP_APPLICATION);
                        Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"接受申请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"接受申请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onApplicationReject(final InvitationInfo invitationInfo) {
            Model.getInstence().getallthreadpool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineApplication(invitationInfo.getGroupInfo().getGroupId(),invitationInfo.getGroupInfo().getGroup_invitePerson(),"拒绝申请");
                        invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.REJECT_GROUP_APPLICATION);
                        Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"拒绝申请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InvitationActivity.this,"拒绝申请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };
    private BroadcastReceiver ContactInvitationChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };
    private BroadcastReceiver GroupInvitationChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        initView();
        initData();
    }

    private void initData() {
        inviteAdapter = new InviteAdapter(this,onClickListener);

        lv_invitation.setAdapter(inviteAdapter);

        refresh();

        //注册监听邀请信息变化的广播
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(ContactInvitationChangedReceiver,new IntentFilter(Constant.CONTACT_INVITATION_CHANGED));
        localBroadcastManager.registerReceiver(GroupInvitationChangedReceiver,new IntentFilter(Constant.GROUP_INVITATIOM_CHANGED));
    }

    private void refresh() {
        //获取邀请数据库中的所有邀请信息
        List<InvitationInfo> invitation = Model.getInstence().getdBmanager().getInvitationTableDao().getInvitation();
        //刷新适配器
        inviteAdapter.refreshData(invitation);
    }

    private void initView() {
        lv_invitation = findViewById(R.id.lv_invitation);
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(ContactInvitationChangedReceiver);
        localBroadcastManager.unregisterReceiver(GroupInvitationChangedReceiver);
        super.onDestroy();
    }
}
