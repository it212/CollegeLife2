package com.it212.collegelife.fragment.mySelf;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it212.collegelife.R;
import com.it212.collegelife.base.BaseFragment;
import com.it212.collegelife.utils.Model;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by imac on 2017/4/8.
 */

public class SETF extends BaseFragment {

    private TextView tv_username;
    private TextView tv_nickname;
    private ImageView iv_icon;
    private LinearLayout ll_userInfo;
    private Button btn_logout;
    private UserInfo userInfo;

    @Override
    public View initView() {
        userInfo = JMessageClient.getMyInfo();
        View view = View.inflate(context, R.layout.setingf, null);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        ll_userInfo = (LinearLayout) view.findViewById(R.id.ll_userInfo);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        tv_username.setText(userInfo.getUserName());
        tv_nickname.setText(userInfo.getNickname());
    }
}
