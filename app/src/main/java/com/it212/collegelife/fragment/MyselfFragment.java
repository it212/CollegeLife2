package com.it212.collegelife.fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.R;
import com.it212.collegelife.activity.InvationActivity;
import com.it212.collegelife.activity.LxrActivity;
import com.it212.collegelife.activity.MsgActivity;
import com.it212.collegelife.activity.MyUserInfo;
import com.it212.collegelife.activity.RegistActivity;
import com.it212.collegelife.activity.UserIcon;
import com.it212.collegelife.base.BaseFragment;
import com.it212.collegelife.utils.Constant;
import com.it212.collegelife.utils.SpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by imac on 2017/4/1.
 */
public class MyselfFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout rl_msg;
    private LinearLayout ll_userInfo;

    private TextView tv_username;

    private PopupWindow popupWindow;
    private ImageView iv_icon;
    private View view;
    private ImageView iv_close;
    private Button btn_login;
    private EditText et_user;
    private EditText et_pwd;
    private ProgressDialog pd_loing;
    private ImageView iv_red;
    private String iconPath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x000:
                    userInfo = JMessageClient.getMyInfo();
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            if (i == 0) {
                                tv_username.setText(userInfo.getUserName());
                                iv_icon.setImageBitmap(bitmap);
                                pd_loing.dismiss();
                            }
                        }
                    });


                    break;
                case 0x001:
                    tv_username.setText("请登录账号！！！");
                    pd_loing.dismiss();
                    Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private TextView tv_discover;
    private TextView tv_setting;
    private TextView tv_lxr;
    private TextView tv_regist;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver LxrChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SpUtils.getInstan().save(SpUtils.IS_NEW_INVITE, true);
            iv_red.setVisibility(View.VISIBLE);
        }
    };
    private UserInfo userInfo;
    private Intent intent;
    private BroadcastReceiver LogoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            iv_icon.setImageResource(R.mipmap.ic_launcher);
            tv_username.setText("请登录账号");
            Toast.makeText(context, "账号在异地登录了", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public View initView() {
        view = View.inflate(context, R.layout.myself, null);
        iv_red = (ImageView) view.findViewById(R.id.iv_red);
        tv_username = (TextView) view.findViewById(R.id.tv_username);

        rl_msg = (RelativeLayout) view.findViewById(R.id.rl_msg);
        ll_userInfo = (LinearLayout) view.findViewById(R.id.ll_userInfo);
        tv_discover = (TextView) view.findViewById(R.id.tv_discover);
        tv_setting = (TextView) view.findViewById(R.id.tv_setting);
        tv_lxr = (TextView) view.findViewById(R.id.tv_lxr);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        userInfo = JMessageClient.getMyInfo();
        if (userInfo == null) {
            iv_icon.setImageResource(R.mipmap.ic_launcher);
            tv_username.setText("请登录账号！！！");
        } else {

            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if (i == 0) {
                        iv_icon.setImageBitmap(bitmap);
                    }
                }
            });

            tv_username.setText(userInfo.getUserName());
        }
        mLBM = LocalBroadcastManager.getInstance(context);
        mLBM.registerReceiver(LxrChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(LogoutReceiver, new IntentFilter(Constant.LoginStateChange));
        rl_msg.setOnClickListener(this);
        ll_userInfo.setOnClickListener(this);

        tv_discover.setOnClickListener(this);
        tv_lxr.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        boolean isNewInvate = SpUtils.getInstan().getBoolean(SpUtils.IS_NEW_INVITE, false);
        iv_red.setVisibility(isNewInvate ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_userInfo:
                isToTagClass(MyUserInfo.class);
                break;
            case R.id.rl_msg:
                isToTagClass(MsgActivity.class);
                break;

            case R.id.iv_close:
                popupWindow.dismiss();
                break;
            case R.id.btn_login:
                final String user = et_user.getText().toString().trim();
                final String pwd = et_pwd.getText().toString().trim();
                if (user.length() > 4) {
                    if (pwd.length() > 4) {
                        popupWindow.dismiss();
                        pd_loing = ProgressDialog.show(context, "提示", "登陆中……");
                        pd_loing.setCanceledOnTouchOutside(true);
                        JMessageClient.login(user, pwd, new BasicCallback(false) {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    JMessageClient.getUserInfo(user, new GetUserInfoCallback(false) {
                                        @Override
                                        public void gotResult(int i, String s, UserInfo userInfo) {
                                            if (i == 0) {
                                                handler.sendEmptyMessage(0x000);
                                            }
                                        }
                                    });


                                } else {
                                    handler.sendEmptyMessage(0x001);
                                }
                            }
                        });


                    } else {
                        Toast.makeText(context, "密码长度必须大于4", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "字符长度必须大于4", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_lxr:
                intent = new Intent(context, LxrActivity.class);
                startActivityForResult(intent, 0x03);
                break;
            case R.id.tv_discover:
                break;
            case R.id.tv_setting:
                break;
            case R.id.tv_regist:
                intent = new Intent(context, RegistActivity.class);
                startActivityForResult(intent, 02);
                break;
        }

    }

    //    跳转到目标界面
    private void isToTagClass(Class<?> cls) {
        userInfo = JMessageClient.getMyInfo();
        if (userInfo == null) {
            newPopwindow();
        } else {
            intent = new Intent(context, cls);
            startActivityForResult(intent, 02);
        }
    }

    private void newPopwindow() {
        popupWindow = new PopupWindow(context);
        View popView = View.inflate(context, R.layout.popwindow, null);
        iv_close = (ImageView) popView.findViewById(R.id.iv_close);
        btn_login = (Button) popView.findViewById(R.id.btn_login);
        et_user = (EditText) popView.findViewById(R.id.et_user);
        et_pwd = (EditText) popView.findViewById(R.id.et_pwd);
        tv_regist = (TextView) popView.findViewById(R.id.tv_regist);
        iv_close.setOnClickListener(MyselfFragment.this);
        btn_login.setOnClickListener(MyselfFragment.this);
        tv_regist.setOnClickListener(this);
        popupWindow.setContentView(popView);
        popupWindow.setWidth(Constant.getWidth(context));
        popupWindow.setHeight(Constant.getHeight(context));
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 02 && resultCode == 02) {
            et_user.setText(data.getStringExtra("name"));
            et_pwd.setText(data.getStringExtra("pwd"));
        } else if (requestCode == 0x03 && resultCode == 0x03) {
            iv_red.setVisibility(View.GONE);
        } else if (requestCode == 02 && resultCode == 01) {
            JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if (i == 0 && bitmap != null) {
                        iv_icon.setImageBitmap(bitmap);
                    }
                }
            });

        } else if (requestCode == 02 && resultCode == 0x07) {
            iv_icon.setImageResource(R.mipmap.ic_launcher);
            tv_username.setText("请登录账号！！！");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(LxrChangeReceiver);
        mLBM.unregisterReceiver(LogoutReceiver);
    }
}
