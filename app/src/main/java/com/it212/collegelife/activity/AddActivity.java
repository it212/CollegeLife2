package com.it212.collegelife.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.it212.collegelife.R;
import com.it212.collegelife.utils.Model;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class AddActivity extends Activity implements View.OnClickListener {

    private RelativeLayout rl_flxr;
    private Button btn_add;
    private ImageView iv_find;
    private EditText et_find;
    private TextView tv_username;
    private UserInfo userInfo;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        rl_flxr= (RelativeLayout) findViewById(R.id.rl_flxr);
        btn_add= (Button) findViewById(R.id.btn_add);
        iv_find= (ImageView) findViewById(R.id.iv_find);
        et_find= (EditText) findViewById(R.id.et_find);
        tv_username= (TextView) findViewById(R.id.tv_username);
        et_find.addTextChangedListener(new TextChangerListener());
        btn_add.setOnClickListener(this);
        iv_find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                ContactManager.sendInvitationRequest(userName, "", "hello", new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        if (0 == responseCode) {
                            //好友请求请求发送成功
                            Toast.makeText(AddActivity.this,"好友请求请求发送成功",Toast.LENGTH_SHORT).show();
                        } else {
                            //好友请求发送失败
                            Toast.makeText(AddActivity.this,"好友请求发送失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.iv_find:
                rl_flxr.setVisibility(View.GONE);
                userName = et_find.getText().toString().trim();
                if (!TextUtils.isEmpty(userName)){
                    JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i==0){
                                rl_flxr.setVisibility(View.VISIBLE);
                                tv_username.setText(userInfo.getUserName());
                            }else {
                                Toast.makeText(AddActivity.this,"请核对用户名（"+ userName +"）是否正确",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(AddActivity.this,"请输入你要查找的联系人账号",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    class TextChangerListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length()>0){
                    iv_find.setVisibility(View.VISIBLE);
                }else {
                    iv_find.setVisibility(View.GONE);
                }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length()>0){
                iv_find.setVisibility(View.VISIBLE);
            }else {
                iv_find.setVisibility(View.GONE);
            }
        }
    }
}
