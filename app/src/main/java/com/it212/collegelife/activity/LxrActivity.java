package com.it212.collegelife.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.R;
import com.it212.collegelife.adapter.LXRAdapter;

import com.it212.collegelife.utils.Constant;
import com.it212.collegelife.utils.Model;
import com.it212.collegelife.utils.SpUtils;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


public class LxrActivity extends Activity implements View.OnClickListener {

    private RelativeLayout rl_invate;
    private ImageView iv_contant_red;
    private ImageView iv_add;
    private ListView lv_msg;
    private TextView tv_new_friend;
    private TextView tv_group;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver ContanctInviteChangerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SpUtils.getInstan().save(SpUtils.IS_NEW_INVITE, true);
            iv_contant_red.setVisibility(View.VISIBLE);

        }
    };
    private LXRAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x000:
                    List<UserInfo> cons = (List<UserInfo>) msg.obj;
                    refresh(cons);
                    break;

                case 0x002:
                    popupWindow.dismiss();
                    Toast.makeText(LxrActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                case 0x001:
                    popupWindow.dismiss();
                    Toast.makeText(LxrActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    getLXRFromNet();
                    break;
                case 0x003:
                    popupWindow.dismiss();
                    Toast.makeText(LxrActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private List<String> list;
    private PopupWindow popupWindow;
    private BroadcastReceiver ContanctChangerREceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("itet", intent.toString());
            getLXRFromNet();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lxr);
        lv_msg = (ListView) findViewById(R.id.lv_msg);
        tv_group = (TextView) findViewById(R.id.tv_group);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_contant_red = (ImageView) findViewById(R.id.iv_contant_red);
        rl_invate = (RelativeLayout) findViewById(R.id.rl_invate);

        boolean isNewInvite = SpUtils.getInstan().getBoolean(SpUtils.IS_NEW_INVITE, true);
        iv_contant_red.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);
        mLBM = LocalBroadcastManager.getInstance(LxrActivity.this);
        mLBM.registerReceiver(ContanctInviteChangerReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(ContanctChangerREceiver, new IntentFilter(Constant.CONTACT_CHANGED));
        iv_add.setOnClickListener(this);
        tv_group.setOnClickListener(this);
        rl_invate.setOnClickListener(this);
        getLXRFromNet();
    }

    private void getLXRFromNet() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    if (userInfoList != null && userInfoList.size() > 0) {
                        for (int i = 0; i < userInfoList.size(); i++) {
                            UserInfo u = userInfoList.get(i);
                            Log.e("user", u.getUserName());
                        }
                        Message msg=new Message();
                        msg.what=0x000;
                        msg.obj=userInfoList;
                        handler.sendMessage(msg);
                    }

                    Toast.makeText(LxrActivity.this, "获取好友列表成功", Toast.LENGTH_SHORT).show();

                } else {
                    //获取好友列表失败
                    handler.sendEmptyMessage(0x001);
                    Toast.makeText(LxrActivity.this, "获取好友列表失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //    刷新视图
    private void refresh(final List<UserInfo> conts) {
//        List<UserInfo>  conts=Model.getInstance().getDbManager().getContactDao().getContacts();
        if (conts != null && conts.size() >= 0) {
            adapter = new LXRAdapter(LxrActivity.this, conts);
            lv_msg.setAdapter(adapter);
            lv_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Conversation.createSingleConversation(conts.get(position).getUserName());
                    Intent intent = new Intent(LxrActivity.this, ChatActivity.class);
                    intent.putExtra("user", conts.get(position).getUserName());
                    startActivityForResult(intent, 02);
                }
            });
            lv_msg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final String name = conts.get(position).getUserName();
                    popupWindow = new PopupWindow(LxrActivity.this);
                    TextView menu = new TextView(LxrActivity.this);
                    menu.setClickable(true);
                    menu.setText("删除" + name);
                    menu.setPadding(20, 0, 20, 0);
                    menu.setTextSize(20);
                    menu.setTextColor(Color.WHITE);
                    menu.setBackgroundColor(Color.RED);
                    menu.setGravity(Gravity.CENTER);
                    menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Model.getInstance().getClobeThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    conts.get(position).removeFromFriendList(new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i==0){
                                                //移出好友列表成功
                                                handler.sendEmptyMessage(0x001);
                                            }else {
                                                //移出好友列表失败
                                                handler.sendEmptyMessage(0x002);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    popupWindow.setContentView(menu);
                    popupWindow.setWidth(lv_msg.getWidth() - 80);
                    popupWindow.setHeight(80);
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(lv_msg, Gravity.CENTER, 0, 0);
                    return true;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 02 && resultCode == 0x0000) {
            getLXRFromNet();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_add:
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_invate:

                UserInfo userInfo= JMessageClient.getMyInfo();
                if (userInfo!=null) {
                    iv_contant_red.setVisibility(View.GONE);
                    SpUtils.getInstan().save(SpUtils.IS_NEW_INVITE, false);
                    setResult(0x03);
                    intent = new Intent(this, InvationActivity.class);
                    startActivityForResult(intent, 02);
                } else {
                    Toast.makeText(LxrActivity.this, "请先登录账号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_group:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(ContanctInviteChangerReceiver);
        mLBM.unregisterReceiver(ContanctChangerREceiver);
    }
}
