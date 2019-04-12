package com.example.asus.bs_im.controller.adapter;

import android.content.Context;
import android.test.mock.MockDialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.SelectContactInfo;
import com.example.asus.bs_im.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectContactsAdapter extends BaseAdapter {
    private Context mContext;
    private List<SelectContactInfo> mSelectContactInfos = new ArrayList<>();
    private List<String> mMembers = new ArrayList<>();//保存群中已经存在的成员
    public SelectContactsAdapter(Context context,List<SelectContactInfo> list,List<String> members) {
        mContext = context;
        if (mSelectContactInfos != null && mSelectContactInfos.size() >= 0){
            mSelectContactInfos.clear();
            mSelectContactInfos.addAll(list);
        }
        mMembers.clear();
        mMembers.addAll(members);
    }

    @Override
    public int getCount() {
        return mSelectContactInfos == null?0: mSelectContactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelectContactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_selected,null);
            holder.checkBox = convertView.findViewById(R.id.cb_selected);
            holder.textView = convertView.findViewById(R.id.tv_selected_name);
            holder.iv_item_select = convertView.findViewById(R.id.iv_item_select);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectContactInfo selectContactInfo = mSelectContactInfos.get(position);
        holder.textView.setText(selectContactInfo.getUserInfo().getNick());
        holder.iv_item_select.setImageDrawable(SpUtils.getInstance().picture(Model.getInstence().getUserAccountTableDao().getPictureFromSql(selectContactInfo.getUserInfo().getUserId())));
        holder.checkBox.setChecked(selectContactInfo.isChecked());
        if (mMembers.contains(selectContactInfo.getUserInfo().getUserId())) {
            holder.checkBox.setChecked(true);
            selectContactInfo.setChecked(true);
        }
        return convertView;
    }

    public List<String> getSelectContacts() {
        List<String> selectors = new ArrayList<>();
        for (SelectContactInfo selectContactInfo : mSelectContactInfos){
            if (selectContactInfo.isChecked()){
                selectors.add(selectContactInfo.getUserInfo().getUserId());
            }
        }
        return selectors;
    }

    private class ViewHolder{
        private CheckBox checkBox;
        private TextView textView;
        private ImageView iv_item_select;
    }
}
