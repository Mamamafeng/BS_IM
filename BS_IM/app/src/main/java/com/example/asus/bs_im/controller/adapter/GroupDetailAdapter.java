package com.example.asus.bs_im.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailAdapter extends BaseAdapter {
    private Context context;
    private boolean isOwner;
    private List<UserInfo> minfoList = new ArrayList<>();
    private boolean isDeleteMode;
    private OnGroupDetailListener onGroupDetailListener;

    public GroupDetailAdapter(Context context,boolean isOwner,OnGroupDetailListener onGroupDetailListener) {
        this.context= context;
        this.isOwner = isOwner;
        this.onGroupDetailListener = onGroupDetailListener;
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
    }

    public void refreshUser(List<UserInfo> infoList){
        if (infoList != null && infoList.size() >= 0){
            minfoList.clear();
            //添加+和-
            initUsers();
            //添加用户
            minfoList.addAll(0,infoList);
            notifyDataSetChanged();
        }
    }

    private void initUsers() {
        //把＋号和-号封装成UserInfo添加到list中
        UserInfo add = new UserInfo("add");
        UserInfo delete = new UserInfo("delete");
        minfoList.add(delete);
        minfoList.add(0,add);
    }

    @Override
    public int getCount() {
        return minfoList == null? 0 : minfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return minfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(this.context, R.layout.group_item,null);

            viewHolder.iv_groupItem_photo = convertView.findViewById(R.id.iv_groupItem_photo);
            viewHolder.iv_groupItem_delete = convertView.findViewById(R.id.iv_groupItem_delete);
            viewHolder.tv_groupItem_name = convertView.findViewById(R.id.tv_groupItem_name);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //获取Item数据
        final UserInfo userInfo = minfoList.get(position);
        //显示数据
        if (isOwner){//群主的相关事件
            if (position == getCount() - 1 ){//-
                if (isDeleteMode){
                    convertView.setVisibility(View.INVISIBLE);
                }else{
                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.iv_groupItem_photo.setImageDrawable(SpUtils.getInstance().picture(
                            Model.getInstence().getUserAccountTableDao().getPictureFromSql(userInfo.getUserId())));
                    viewHolder.iv_groupItem_delete.setVisibility(View.GONE);
                    viewHolder.tv_groupItem_name.setVisibility(View.INVISIBLE);
                }
            }else if(position == getCount() - 2 ){//+
                if (isDeleteMode){
                    convertView.setVisibility(View.INVISIBLE);
                }else{
                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.iv_groupItem_photo.setImageDrawable(SpUtils.getInstance().picture(
                            Model.getInstence().getUserAccountTableDao().getPictureFromSql(userInfo.getUserId())));
                    viewHolder.iv_groupItem_delete.setVisibility(View.GONE);
                    viewHolder.tv_groupItem_name.setVisibility(View.INVISIBLE);
                }
            }else {//用户
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tv_groupItem_name.setVisibility(View.VISIBLE);
                viewHolder.tv_groupItem_name.setText(userInfo.getNick());
                viewHolder.iv_groupItem_photo.setImageDrawable(SpUtils.getInstance().picture(
                        Model.getInstence().getUserAccountTableDao().getPictureFromSql(userInfo.getUserId())));
                if (isDeleteMode){
                    viewHolder.iv_groupItem_delete.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iv_groupItem_delete.setVisibility(View.GONE);
                }
            }
            //点击事件
            if (position == getCount() - 1){//减号的处理
                viewHolder.iv_groupItem_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isDeleteMode){
                            isDeleteMode = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if (position == getCount() - 2){//加号的处理
                viewHolder.iv_groupItem_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGroupDetailListener.onAddMembers();
                    }
                });
            }else {//删除按钮的处理
                viewHolder.iv_groupItem_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGroupDetailListener.onDeleteMembers(userInfo);
                    }
                });
            }
        }else{//普通成员的相关事件
            if (position == getCount() - 1 || position == getCount() - 2){
                convertView.setVisibility(View.GONE);
            }else{
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tv_groupItem_name.setText(userInfo.getNick());
                viewHolder.iv_groupItem_photo.setImageDrawable(SpUtils.getInstance().picture(
                        Model.getInstence().getUserAccountTableDao().getPictureFromSql(userInfo.getUserId())));
                viewHolder.iv_groupItem_delete.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    private class ViewHolder{

        private ImageView iv_groupItem_photo;
        private TextView tv_groupItem_name;
        private ImageView iv_groupItem_delete;
    }

    public interface OnGroupDetailListener{
        //添加群成员方法
        void onAddMembers();
        //删除群成员方法
        void onDeleteMembers(UserInfo userInfo);
    }
}
