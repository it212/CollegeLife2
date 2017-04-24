package com.it212.collegelife.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import org.xutils.x;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;


/**
 * Created by imac on 2017/4/1.
 */

public class CLApp extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        inits();

    }

    private void inits() {

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
//// 如果APP启用了远程的service，此application:onCreate会被调用2次
//// 为了防止SDK被初始化2次，加此判断会保证SDK被初始化1次
//// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
//建议将AnSocial做成一个帮助类, 使用单例类模式
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this,true);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        x.Ext.setDebug(true);
        x.Ext.init(this);
        Model.getInstance().init(this);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public  static Context getAppContext(){
        return mContext;
    }


}
