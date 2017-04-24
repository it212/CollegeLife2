package com.it212.collegelife.base;

import android.content.Context;
import android.view.View;

/**
 * Created by imac on 2017/4/9.
 */

public abstract class BasePager {
    public   Context context;
    public View rootView;
    public boolean isInitData;
    public BasePager(Context context){
       this. context=context;
        rootView=initView();
    }

    public abstract View initView();
    public void  initData(){

    }
}
