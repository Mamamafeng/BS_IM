package com.example.asus.bs_im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.InvitationInfo;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class InviteAdapter extends BaseAdapter {

    private Context mContext;
    private List<InvitationInfo> minvitationInfos = new ArrayList<>();
    private OnClickListener mOnClickListener;
    private InvitationInfo invitationInfo;

    public InviteAdapter(Context context, OnClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
    }

    //刷新数据
    public void refreshData(List<InvitationInfo> invitationInfo){

        if (invitationInfo != null && invitationInfo.size() >= 0){
            minvitationInfos.clear();
            minvitationInfos.addAll(invitationInfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return minvitationInfos == null ? 0 : minvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return minvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取或创建一个ViewHolder
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.invitation_item,null);

            viewHolder.iv_invite_logo = convertView.findViewById(R.id.iv_invite_logo);
            viewHolder.tv_invite_name = convertView.findViewById(R.id.tv_invite_name);
            viewHolder.tv_invite_reason = convertView.findViewById(R.id.tv_invite_reason);
            viewHolder.bt_invite_accept = convertView.findViewById(R.id.bt_invite_accept);
            viewHolder.bt_invite_reject = convertView.findViewById(R.id.bt_invite_reject);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //获取当前Item数据
        invitationInfo = minvitationInfos.get(position);
        //展示数据
        UserInfo user = invitationInfo.getUserInfo();
        if (user != null){
            //联系人的邀请信息
            viewHolder.iv_invite_logo.setImageDrawable(SpUtils.getInstance().picture(Model.getInstence().getUserAccountTableDao().getPictureFromSql(user.getUserId())));
            viewHolder.tv_invite_name.setText(invitationInfo.getUserInfo().getNick());

            viewHolder.bt_invite_accept.setVisibility(View.GONE);
            viewHolder.bt_invite_reject.setVisibility(View.GONE);
            if (invitationInfo.getInvitationStatus() == InvitationInfo.InvitationStatusn.NEW_INVITATION){
                if (invitationInfo.getInvitationReason() == null){
                    viewHolder.tv_invite_reason.setText("添加好友");
                }else{
                    viewHolder.tv_invite_reason.setText(invitationInfo.getInvitationReason());
                }

                viewHolder.bt_invite_accept.setVisibility(View.VISIBLE);
                viewHolder.bt_invite_reject.setVisibility(View.VISIBLE);

                viewHolder.bt_invite_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClickListener.onAccept(invitationInfo);
                    }
                });
                viewHolder.bt_invite_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       mOnClickListener.onReject(invitationInfo);
                    }
                });
            }else if (invitationInfo.getInvitationStatus() == InvitationInfo.InvitationStatusn.INVITATION_ACCEPT){
                if (invitationInfo.getInvitationReason() == null){
                    viewHolder.tv_invite_reason.setText("接受邀请");
                }else{
                    viewHolder.tv_invite_reason.setText(invitationInfo.getInvitationReason());
                }
            }else if (invitationInfo.getInvitationStatus() == InvitationInfo.InvitationStatusn.INVITATION_ACCEPTED_BY_PEER){
                if (invitationInfo.getInvitationReason() == null){
                    viewHolder.tv_invite_reason.setText("邀请被接受");
                }else{
                    viewHolder.tv_invite_reason.setText(invitationInfo.getInvitationReason());
                }
            }
        }else{//群组的邀请信息
            //显示名称
            viewHolder.tv_invite_name.setText(invitationInfo.getGroupInfo().getGroup_invitePerson());
            viewHolder.bt_invite_accept.setVisibility(View.GONE);
            viewHolder.bt_invite_reject.setVisibility(View.GONE);
            //显示原因
            switch(invitationInfo.getInvitationStatus()){
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    viewHolder.tv_invite_reason.setText("您的群申请请已经被接受");
                    break;
                //  您的群邀请已经被接收
                case GROUP_INVITATION_ACCEPTED:
                    viewHolder.tv_invite_reason.setText("您的群邀请已经被接收");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_REJECTED:
                    viewHolder.tv_invite_reason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITATION_REJECT:
                    viewHolder.tv_invite_reason.setText("您的群邀请已经被拒绝");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITATION:
                    viewHolder.bt_invite_accept.setVisibility(View.VISIBLE);
                    viewHolder.bt_invite_reject.setVisibility(View.VISIBLE);

                    // 接受邀请
                    viewHolder.bt_invite_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onGroupInvitationAccept(invitationInfo);
                        }
                    });

                    // 拒绝邀请
                    viewHolder.bt_invite_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onGroupInvitationReject(invitationInfo);
                        }
                    });

                    viewHolder.tv_invite_reason.setText("您收到了群邀请");
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    viewHolder.bt_invite_accept.setVisibility(View.VISIBLE);
                    viewHolder.bt_invite_reject.setVisibility(View.VISIBLE);

                    // 接受申请
                    viewHolder.bt_invite_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onApplicationAccept(invitationInfo);
                        }
                    });

                    // 拒绝申请
                    viewHolder.bt_invite_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onApplicationReject(invitationInfo);
                        }
                    });

                    viewHolder.tv_invite_reason.setText("您收到了群申请");
                    break;

                // 你接受了群邀请
                case ACCEPTE_GROUP_INVITATION:
                    viewHolder.tv_invite_reason.setText("你接受了群邀请");
                    break;

                // 您批准了群申请
                case ACCEPTE_GROUP_APPLICATION:
                    viewHolder.tv_invite_reason.setText("您批准了群申请");
                    break;

                // 您拒绝了群邀请
                case REJECT_GROUP_INVITATION:
                    viewHolder.tv_invite_reason.setText("您拒绝了群邀请");
                    break;

                // 您拒绝了群申请
                case REJECT_GROUP_APPLICATION:
                    viewHolder.tv_invite_reason.setText("您拒绝了群申请");
                    break;
            }
        }
        //返回View
        return convertView;
    }

    private class ViewHolder{
        private ImageView iv_invite_logo;
        private TextView tv_invite_name;
        private TextView tv_invite_reason;
        private Button bt_invite_accept;
        private Button bt_invite_reject;
    }
    public interface OnClickListener {
        //联系人的点击事件
        void onAccept(InvitationInfo invitationInfo);
        void onReject(InvitationInfo invitationInfo);
        //群邀请的点击事件
        void onGroupInvitationAccept(InvitationInfo invitationInfo);
        void onGroupInvitationReject(InvitationInfo invitationInfo);
        //申请的点击事件
        void onApplicationAccept(InvitationInfo invitationInfo);
        void onApplicationReject(InvitationInfo invitationInfo);
    }
}
