package com.it212.collegelife.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.it212.collegelife.R;
import com.it212.collegelife.activity.ChatActivity;
import com.it212.collegelife.domain.InvationInfo;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * Created by imac on 2017/4/11.
 */

public class EventListener {
    private final Context mContext;

    private final LocalBroadcastManager mLBM;


    public EventListener(Context context){
        this.mContext=context;
        mLBM = LocalBroadcastManager.getInstance(context);
        JMessageClient.registerEventReceiver( this);
    }
    public void onEvent(MessageEvent event){
        Message msg = event.getMessage();

        switch (msg.getContentType()){
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
               String jmsg= textContent.getText();
                Intent intent=new Intent(Constant.MSG_CHANGE);
                intent.putExtra("text",jmsg);
                mLBM.sendBroadcast(intent);
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent)msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()){
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;
        }
    }
    public void onEvent(LoginStateChangeEvent event){
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        Intent intent=null;
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
//                NotificationManager manager= (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//               Notification.Builder builder= new Notification.Builder(mContext);
//                builder.setAutoCancel(true);
//                builder.setSmallIcon(R.drawable.add);
//                builder.setContentText("用户密码在服务器端被修改");
//                builder.setContentTitle("提示");
//                builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
//                Intent intent=new Intent(mContext,ChatActivity.class);
//                PendingIntent pendingIntent=PendingIntent.getActivity(mContext,1,intent,PendingIntent.FLAG_ONE_SHOT);
//                builder.setContentIntent(pendingIntent);
//                manager.notify(0,builder.build());
                intent=new Intent(Constant.LoginStateChange);
                intent.putExtra("username",myInfo.getUserName());
                mLBM.sendBroadcast(intent);
                break;
            case user_logout:
                //用户换设备登录
                 intent=new Intent(Constant.LoginStateChange);
                intent.putExtra("username",myInfo.getUserName());
                mLBM.sendBroadcast(intent);
                break;
            case user_deleted:
                //用户被删除
                intent=new Intent(Constant.LoginStateChange);
                intent.putExtra("username",myInfo.getUserName());
                mLBM.sendBroadcast(intent);
                break;
        }
    }
    public void onEvent(NotificationClickEvent event){
        Intent notificationIntent = new Intent(mContext, ChatActivity.class);
        mContext.startActivity(notificationIntent);//自定义跳转到指定页面
    }
    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();
        Intent intent=null;
        switch (event.getType()) {
            case invite_received://收到好友邀请
                //...
                InvationInfo invitionInfo=new InvationInfo();
                invitionInfo.setReason(reason);
                invitionInfo.setUserName(fromUsername);
                Model.getInstance().getInviteDao().add(invitionInfo);
               intent=new Intent(Constant.CONTACT_INVITE_CHANGED);
                intent.putExtra("reason",reason);
                intent.putExtra("user",fromUsername);
                mLBM.sendBroadcast(intent);
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                intent=new Intent(Constant.CONTACT_INVITE_CHANGED);
                intent.putExtra("reason",reason);
                intent.putExtra("user",fromUsername);
                mLBM.sendBroadcast(intent);
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                intent=new Intent(Constant.CONTACT_INVITE_CHANGED);
                intent.putExtra("reason",reason);
                intent.putExtra("user",fromUsername);
                mLBM.sendBroadcast(intent);
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                intent=new Intent(Constant.CONTACT_INVITE_CHANGED);
                intent.putExtra("reason",reason);
                intent.putExtra("user",fromUsername);
                mLBM.sendBroadcast(intent);
                break;
            default:
                break;
        }
    }
}
