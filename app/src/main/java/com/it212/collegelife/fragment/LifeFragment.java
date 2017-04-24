package com.it212.collegelife.fragment;

import android.view.View;


import com.it212.collegelife.R;

import com.it212.collegelife.base.BaseFragment;



/**
 * Created by imac on 2017/4/1.
 */
public class LifeFragment extends BaseFragment {

    private View view;


    @Override
    public View initView() {

        view = View.inflate(context, R.layout.life_fragment, null);



        return view;
    }



    @Override
    public void initData() {
        super.initData();

    }

}
