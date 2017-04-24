package com.it212.collegelife.utils;

import android.content.Context;
import android.text.TextUtils;


import com.it212.collegelife.sqlite.dao.InviteDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by junhua on 2017/4/1.
 */

public class Model {
    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();
    private static Model model = new Model();

    private EventListener eventListener;
    private InviteDao dao;
    private String path;



    private Model() {
    }



    public static Model getInstance() {
        return model;
    }

    public void init(Context context) {
        mContext = context;
        eventListener = new EventListener(mContext);
        path = context.getFilesDir().getAbsolutePath();
    }

    public ExecutorService getClobeThreadPool() {
        return executors;
    }


    public void loginSuccess(String account){
        if (TextUtils.isEmpty(account)){
            return;
        }
        dao = new InviteDao(mContext);


    }
    public InviteDao getInviteDao() {
        return dao;
    }
}
