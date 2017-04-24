package com.it212.collegelife.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.it212.collegelife.R;
import com.it212.collegelife.activity.MainActivity;
import com.it212.collegelife.activity.MapActivity;
import com.it212.collegelife.base.BaseFragment;


/**
 * Created by imac on 2017/4/1.
 */
public class ServiceFragment extends BaseFragment implements View.OnClickListener {


    private MainActivity mainActivity;
    private Button btn_map;

    @Override
    public View initView() {
        mainActivity = (MainActivity) context;
        View view = View.inflate(context, R.layout.servief, null);
        btn_map = (Button) view.findViewById(R.id.btn_map);

        return view;
    }


    @Override
    public void initData() {
        btn_map.setOnClickListener(this);

    }

    /* 头像文件 */


    public void upData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
                Intent intent = new Intent(context, MapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
