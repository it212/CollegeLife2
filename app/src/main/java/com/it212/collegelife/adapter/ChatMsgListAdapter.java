package com.it212.collegelife.adapter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it212.collegelife.R;
import com.it212.collegelife.activity.ChatActivity;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by imac on 2017/4/23.
 */

public class ChatMsgListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Message> msgList;
    private Message msg;
    private UserInfo mUserInfo;
    private TextContent textContent;
    private String name;

    public ChatMsgListAdapter(Context context, List<Message> msgList) {
        this.context = context;
        this.msgList = msgList;
        mUserInfo = JMessageClient.getMyInfo();
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        msg = msgList.get(position);
        TextHolder textHolder = null;
        ImgHolder imgHolder = null;
        VoiceHolder voiceHolder = null;
        if (convertView == null) {
            switch (msg.getContentType()) {
                case text:
                    textHolder = new TextHolder();
                    convertView = View.inflate(context, R.layout.chat_text, null);
                    textHolder.ll_left_msg = (LinearLayout) convertView.findViewById(R.id.ll_left_msg);
                    textHolder.tv_left_msg = (TextView) convertView.findViewById(R.id.tv_left_msg);
                    textHolder.ll_right_msg = (LinearLayout) convertView.findViewById(R.id.ll_right_msg);
                    textHolder.tv_right_msg = (TextView) convertView.findViewById(R.id.tv_right_msg);
                    textHolder.ll_left_msg.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    convertView.setTag(textHolder);
                    break;
                case image:
                    imgHolder = new ImgHolder();
                    convertView = View.inflate(context, R.layout.chat_img, null);
                    imgHolder.ll_left_msg = (LinearLayout) convertView.findViewById(R.id.ll_left_msg);
                    imgHolder.iv_left_msg = (ImageView) convertView.findViewById(R.id.iv_left_msg);
                    imgHolder.ll_right_msg = (LinearLayout) convertView.findViewById(R.id.ll_right_msg);
                    imgHolder.iv_right_msg = (ImageView) convertView.findViewById(R.id.iv_right_msg);
                    convertView.setTag(imgHolder);

                    break;
                case video:
                    voiceHolder = new VoiceHolder();
                    convertView = View.inflate(context, R.layout.chat_voice, null);
                    voiceHolder.ll_left_msg = (LinearLayout) convertView.findViewById(R.id.ll_left_msg);
                    voiceHolder.iv_left_msg = (ImageView) convertView.findViewById(R.id.iv_left_msg);
                    voiceHolder.ll_right_msg = (LinearLayout) convertView.findViewById(R.id.ll_right_msg);
                    voiceHolder.iv_right_msg = (ImageView) convertView.findViewById(R.id.iv_right_msg);
                    convertView.setTag(voiceHolder);
                    break;
            }
        } else {
            switch (msg.getContentType()){
                case text:
                    textHolder = (TextHolder) convertView.getTag();
                    break;
                case image:
                    imgHolder = (ImgHolder) convertView.getTag();
                    break;
                case voice:
                    voiceHolder = (VoiceHolder) convertView.getTag();
                    break;
            }
        }

        switch (msg.getContentType()) {
            case text:
                textContent = (TextContent) msg.getContent();
                name = msg.getFromUser().getUserName();
                if (name.equals(mUserInfo.getUserName())) {
                    textHolder.ll_left_msg.setVisibility(View.GONE);
                    textHolder.ll_right_msg.setVisibility(View.VISIBLE);
                    textHolder.tv_right_msg.setText(textContent.getText());
                } else {
                    textHolder.ll_left_msg.setVisibility(View.VISIBLE);
                    textHolder.ll_right_msg.setVisibility(View.GONE);
                    textHolder.tv_left_msg.setText(textContent.getText());
                }
                break;
            case image:
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                name = msg.getFromUser().getUserName();
                if (name.equals(mUserInfo.getUserName())) {
                    imgHolder.ll_left_msg.setVisibility(View.GONE);
                    imgHolder.ll_right_msg.setVisibility(View.VISIBLE);

                } else {
                    imgHolder.ll_left_msg.setVisibility(View.VISIBLE);
                    imgHolder.ll_right_msg.setVisibility(View.GONE);
                }
                break;
            case voice:
                break;
        }

        return convertView;
    }

    static class TextHolder {
        LinearLayout ll_left_msg;
        TextView tv_left_msg;
        LinearLayout ll_right_msg;
        TextView tv_right_msg;
    }

    static class ImgHolder {
        LinearLayout ll_left_msg;
        ImageView iv_left_msg;
        LinearLayout ll_right_msg;
        ImageView iv_right_msg;
    }

    static class VoiceHolder {
        LinearLayout ll_left_msg;
        ImageView iv_left_msg;
        LinearLayout ll_right_msg;
        ImageView iv_right_msg;
    }
}
