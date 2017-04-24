package com.it212.collegelife.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it212.collegelife.R;
import com.it212.collegelife.utils.Constant;

public class UserIcon extends Activity implements View.OnClickListener {

    private RelativeLayout activity_user_icon;
    private ImageView iv_icon;
    private PopupWindow popupWindow;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_icon);
        activity_user_icon= (RelativeLayout) findViewById(R.id.activity_user_icon);
        iv_icon= (ImageView) findViewById(R.id.iv_icon);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        iv_icon.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_icon:
                newPopwin();
                break;
            case R.id.ll_bg:
                popupWindow.dismiss();
                break;
            case R.id.tv_img:
                popupWindow.dismiss();
                btn_ok.setEnabled(true);
                break;
            case R.id.tv_photo:
                popupWindow.dismiss();
                btn_ok.setEnabled(true);
                break;
            case R.id.btn_ok:
               finish();
                break;
        }
    }

    private void newPopwin() {
        popupWindow = new PopupWindow(this);
        popupWindow.setFocusable(true);
        View view=View.inflate(this,R.layout.select_icon_menu,null);
        LinearLayout ll_bg= (LinearLayout) view.findViewById(R.id.ll_bg);
        TextView tv_img= (TextView) view.findViewById(R.id.tv_img);
        TextView tv_photo= (TextView) view.findViewById(R.id.tv_photo);
        ll_bg.setOnClickListener(this);
        tv_img.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
        popupWindow.setContentView(view);
        popupWindow.setWidth(Constant.getWidth(this));
        popupWindow.setHeight(Constant.getHeight(this));
        popupWindow.showAtLocation(activity_user_icon, Gravity.CENTER,0,0);
    }
}
