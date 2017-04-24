package com.it212.collegelife.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it212.collegelife.R;

import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by imac on 2017/4/13.
 */
public class LXRAdapter extends BaseAdapter {
    private Context context;
    private  List<UserInfo> conts;
    private ViewHolder holder;

    public LXRAdapter(Context context, List<UserInfo> conts) {
        this.context = context;
        this.conts = conts;
    }

    @Override
    public int getCount() {
        return conts.size();
    }

    @Override
    public Object getItem(int position) {
        return conts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.lxr_list_item, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserInfo userInfo = conts.get(position);
        holder.tv_user.setText(userInfo.getUserName());
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (i==0){
                    holder.iv_icon.setImageBitmap(bitmap);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_user;
    }
}
