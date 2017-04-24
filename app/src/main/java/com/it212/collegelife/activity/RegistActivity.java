package com.it212.collegelife.activity;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.R;
import com.it212.collegelife.utils.Model;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


public class RegistActivity extends Activity {

    private EditText et_user;
    private EditText et_pwd;
    private EditText et_dwpwd;
    private Button btn_regist;
    private TextView tv_user_tips;
    private TextView tv_pwd_tips;
    private TextView tv_dwpwd_tips;
    private ProgressDialog pd;
    private String user;
    private String pwd;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x000:
                    pd.dismiss();
                    Intent intent = new Intent(RegistActivity.this, UserIcon.class);
                    intent.putExtra("user", user);
                    intent.putExtra("pwd", pwd);
                    startActivity(intent);
                    finish();

                    break;
                case 0x001:
                    Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regest);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_dwpwd = (EditText) findViewById(R.id.et_dwpwd);
        tv_user_tips = (TextView) findViewById(R.id.tv_user_tips);
        tv_pwd_tips = (TextView) findViewById(R.id.tv_pwd_tips);
        tv_dwpwd_tips = (TextView) findViewById(R.id.tv_dwpwd_tips);

        et_user.addTextChangedListener(new userListener());
        et_pwd.addTextChangedListener(new pwdListener());
        et_dwpwd.addTextChangedListener(new dwpwdListener());
        btn_regist = (Button) findViewById(R.id.btn_regist);
        btn_regist.setEnabled(false);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = ProgressDialog.show(RegistActivity.this, "提示", "正在注册中……");
                pd.setCanceledOnTouchOutside(true);
                user = et_user.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();
                String dwpwd = et_dwpwd.getText().toString().trim();

                if (pwd.equals(dwpwd)) {
                Model.getInstance().getClobeThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        JMessageClient.register(user, pwd, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    handler.sendEmptyMessage(0x000);
                                } else {
                                    handler.sendEmptyMessage(0x001);
                                }
                            }
                        });
                    }
                });

                }
            }
        });
    }
    class userListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() <= 4) {
                tv_user_tips.setVisibility(View.VISIBLE);
                tv_user_tips.setText("账号长度必须要大于4");
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 4) {
                tv_user_tips.setVisibility(View.VISIBLE);
                tv_user_tips.setText("账号长度必须要大于4");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() <= 4) {
                tv_user_tips.setVisibility(View.VISIBLE);
                tv_user_tips.setText("账号长度必须要大于4");
                btn_regist.setEnabled(false);
            }else if(s.length()>4){
                tv_user_tips.setVisibility(View.INVISIBLE);
                String pwd = et_pwd.getText().toString().trim();
                String dwpwd = et_dwpwd.getText().toString().trim();
                if ( pwd.length()>4 && pwd.equals(dwpwd)){
                    btn_regist.setEnabled(true);
                }
            }
        }
    }

    class pwdListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() <= 4) {
                tv_pwd_tips.setText("密码长度必须要大于4");
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 4) {
                tv_pwd_tips.setVisibility(View.VISIBLE);
                tv_pwd_tips.setText("密码长度必须要大于4");
                btn_regist.setEnabled(false);
            } else {
                String dwpwd = et_dwpwd.getText().toString().trim();
                if (!dwpwd.equals(s.toString())) {
                    tv_pwd_tips.setVisibility(View.VISIBLE);
                    tv_pwd_tips.setText("输入两次密码不一致");
                    btn_regist.setEnabled(false);
                } else {
                    tv_pwd_tips.setVisibility(View.INVISIBLE);
                    tv_dwpwd_tips.setVisibility(View.INVISIBLE);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() <= 4) {
                tv_pwd_tips.setVisibility(View.VISIBLE);
                tv_pwd_tips.setText("密码长度必须要大于4");
                btn_regist.setEnabled(false);
            } else {
                String dwpwd = et_dwpwd.getText().toString().trim();
                if (!dwpwd.equals(s.toString()) && dwpwd.length() > 0) {
                        btn_regist.setEnabled(false);
                } else {
                    tv_pwd_tips.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class dwpwdListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String pwd = et_pwd.getText().toString().trim();
            if (!pwd.equals(s.toString())) {
                tv_dwpwd_tips.setVisibility(View.VISIBLE);
                tv_dwpwd_tips.setText("输入两次密码不一致");
                btn_regist.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 4) {
                tv_dwpwd_tips.setVisibility(View.VISIBLE);
                tv_dwpwd_tips.setText("密码长度必须要大于4");
                btn_regist.setEnabled(false);
            } else {
                String pwd = et_pwd.getText().toString().trim();
                if (!pwd.equals(s.toString())) {
                    tv_dwpwd_tips.setVisibility(View.VISIBLE);
                    tv_dwpwd_tips.setText("输入两次密码不一致");
                    btn_regist.setEnabled(false);
                } else {
                    tv_pwd_tips.setVisibility(View.INVISIBLE);
                    tv_dwpwd_tips.setVisibility(View.INVISIBLE);
                    btn_regist.setEnabled(true);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 4) {
                tv_dwpwd_tips.setVisibility(View.VISIBLE);
                tv_dwpwd_tips.setText("字符长度必须要大于4");
            } else {
                String pwd = et_pwd.getText().toString().trim();
                if (!pwd.equals(s.toString())) {
                    tv_dwpwd_tips.setVisibility(View.VISIBLE);
                    tv_dwpwd_tips.setText("输入两次密码不一致");
                } else {
                    tv_dwpwd_tips.setVisibility(View.INVISIBLE);
                    btn_regist.setEnabled(true);
                }
            }
        }
    }

}
