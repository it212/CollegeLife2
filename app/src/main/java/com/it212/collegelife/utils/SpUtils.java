package com.it212.collegelife.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by imac on 2017/4/11.
 */

public class SpUtils {

    public static final String IS_NEW_INVITE = "is_new_invite";
    public static final String USER_NAME = "user_name";
    public static  final String IS_OPEN="is_open";
    public static final String USER_ICON_PATH = "user_icon_path";
    private  static SpUtils spUtils=new SpUtils();
    private static SharedPreferences msp;

    private SpUtils(){

    }
    public static SpUtils getInstan(){
        if (msp==null){
            msp = CLApp.getAppContext().getSharedPreferences("life", Context.MODE_PRIVATE);
        }
        return spUtils;
    }
    public void save(String key ,Object values){
        if (values instanceof  String){
            msp.edit().putString(key, (String) values).commit();
        }else   if (values instanceof  Integer){
            msp.edit().putInt(key, (Integer) values).commit();
        }else   if (values instanceof  Boolean){
            msp.edit().putBoolean(key, (Boolean) values).commit();
        }
    }
    public String getString(String key,String deav){
        return msp.getString(key,deav);
    }
    public int getInt(String key ,int deav){
        return msp.getInt(key,deav);
    }
    public boolean getBoolean(String key,boolean deav){
        return msp.getBoolean(key,deav);
    }
}
