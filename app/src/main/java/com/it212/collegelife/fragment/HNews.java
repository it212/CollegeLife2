package com.it212.collegelife.fragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.base.BaseFragment;

/**
 * Created by imac on 2017/4/8.
 */

public class HNews extends BaseFragment {



    @Override
    public View initView() {
        TextView textView=new TextView(context);
        textView.setText("6666666666666666");
        textView.setClickable(true);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }

}
