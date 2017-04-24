package com.it212.collegelife.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.R;
import com.it212.collegelife.adapter.ChatMsgListAdapter;
import com.it212.collegelife.fragment.mySelf.TypeF;
import com.it212.collegelife.utils.Constant;
import com.it212.collegelife.utils.Model;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

public class ChatActivity extends FragmentActivity implements View.OnClickListener {

    private TextView tv_user;
    private ImageView iv_delete;
    private String user;
    private ImageView iv_change;
    private ImageView iv_tpye;
    private EditText et_input;
    private TextView tv_voi;
    private Button btn_send;
    private FrameLayout fl_type;
    private boolean isKeyB = true;
    private boolean isMType;
    private String text_msg;
    private ListView lv_msg_list;
    private ImageView iv_voice;
    private FragmentTransaction ft;

    private Context mContext;
    private Conversation mConversation;
    private ChatMsgListAdapter adapter;
    private List<Message> msgList;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver msgReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text=intent.getStringExtra("text");
            Toast.makeText(ChatActivity.this, "收到消息"+text, Toast.LENGTH_LONG).show();
        }
    };

    public Context getmContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        tv_user = (TextView) findViewById(R.id.tv_user);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_change = (ImageView) findViewById(R.id.iv_change);
        et_input = (EditText) findViewById(R.id.et_input);
        btn_send = (Button) findViewById(R.id.btn_send);
        iv_tpye = (ImageView) findViewById(R.id.iv_tpye);
        lv_msg_list = (ListView) findViewById(R.id.lv_msg_list);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        tv_voi = (TextView) findViewById(R.id.tv_voi);
        fl_type = (FrameLayout) findViewById(R.id.fl_type);

        mLBM = LocalBroadcastManager.getInstance(ChatActivity.this);
        mLBM.registerReceiver(msgReceiver,new IntentFilter(Constant.MSG_CHANGE));
        tv_voi.setOnClickListener(this);
        tv_voi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_voice.setVisibility(View.VISIBLE);
                        break;

                    case MotionEvent.ACTION_UP:
                        iv_voice.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
        iv_change.setOnClickListener(this);
        iv_tpye.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        et_input.addTextChangedListener(new InputListener());
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        if (!TextUtils.isEmpty(user)) {
         mConversation =   Conversation.createSingleConversation(user);
            msgList = mConversation.getAllMessage();
            tv_user.setText(user);
            lv_msg_list.setBackgroundColor(Color.GRAY);
            adapter=new ChatMsgListAdapter(ChatActivity.this,msgList);
            lv_msg_list.setAdapter(adapter);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tpye:
                fl_type.setVisibility(View.VISIBLE);
                ft = getSupportFragmentManager().beginTransaction();
                break;
            case R.id.iv_change:
                fl_type.setVisibility(View.GONE);
                if (!isKeyB) {
                    isKeyB = true;
                    et_input.setVisibility(View.VISIBLE);
                    iv_change.setBackgroundResource(R.drawable.ease_chatting_setmode_voice_btn_normal);
                    tv_voi.setVisibility(View.GONE);
                } else {
                    isKeyB = false;
                    et_input.setVisibility(View.GONE);
                    iv_change.setBackgroundResource(R.drawable.ease_chatting_setmode_keyboard_btn_normal);

                    tv_voi.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_send:
                text_msg = et_input.getText().toString().trim();
                fl_type.setVisibility(View.GONE);
                Message message = mConversation.createSendMessage(new TextContent(text_msg));
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseDesc) {
                        if (responseCode == 0) {
                            //消息发送成功
                            Toast.makeText(ChatActivity.this, "消息发送成功", Toast.LENGTH_LONG).show();
                        } else {
                            //消息发送失败
                            Toast.makeText(ChatActivity.this, "消息发送失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                JMessageClient.sendMessage(message);
                adapter.notifyDataSetChanged();
                et_input.setText("");
                break;
            case R.id.iv_delete:
                AlertDialog dialog = new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("提示：")
                        .setMessage("确定要删除好友吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Model.getInstance().getClobeThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;

        }
    }


    class InputListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() > 0) {
                iv_tpye.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            } else {
                btn_send.setVisibility(View.GONE);
                iv_tpye.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                iv_tpye.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            } else {
                btn_send.setVisibility(View.GONE);
                iv_tpye.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(msgReceiver);
    }
}
