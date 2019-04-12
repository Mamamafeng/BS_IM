package com.example.asus.bs_im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.bs_im.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends BaseAdapter {

    private Context mcotext;
    private List<EMGroup> mgroups = new ArrayList<>();
    public GroupListAdapter(Context context) {
        mcotext = context;
    }

    public void refresh(List<EMGroup> groups){
        if (groups != null && groups.size() >= 0){
            mgroups.clear();
            mgroups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mgroups == null?0:mgroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mgroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null){
            holder = new viewHolder();
            convertView = View.inflate(mcotext, R.layout.grouplist,null);
            holder.name = convertView.findViewById(R.id.tv_group_item);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        EMGroup emGroup = mgroups.get(position);

        holder.name.setText(emGroup.getGroupName());

        return convertView;
    }

    private class viewHolder{
        TextView name;
    }
}
