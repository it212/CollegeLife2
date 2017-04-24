package com.it212.collegelife.fragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.base.BaseFragment;

/**
 * Created by imac on 2017/4/8.
 */

public class HSchool extends BaseFragment {




    @Override
    public View initView() {
        TextView textView=new TextView(context);
        textView.setText("88888888888888888");
        textView.setClickable(true);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }

}
