package com.it212.collegelife.fragment.mySelf;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.base.BaseFragment;

/**
 * Created by imac on 2017/4/8.
 */

public class TypeF extends BaseFragment {

    @Override
    public View initView() {
        TextView textView=new TextView(context);
        textView.setText("+++++++");
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了",Toast.LENGTH_SHORT).show();

            }
        });
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }

}
