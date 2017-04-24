package com.it212.collegelife.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by imac on 2017/4/11.
 */

public class Constant {
    public static final String CONTACT_CHANGED = "contact_changed";// 发送联系人变化的广播
    public static final String CONTACT_INVITE_CHANGED = "contact_invite_changed";// 联系人邀请信息变化的广播
    public static final String GROUP_INVITE_CHANGED = "group_invite_changed";// 群邀请信息变化的广播
    public static final String GROUP_ID = "group_id";// 群id
    public static final String EXIT_GROUP = "exit_group";// 退群广播
    public static final String LoginStateChange= "LoginStateChange";// 登陆状态发生变化
    public static final String MSG_CHANGE= "msgchange";// 收到消息
    public static final String MAIN_PATH=  CLApp.getAppContext().getFilesDir().getAbsolutePath();;// 根目录

    /**
     * 宽
     *
     * @return
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 高
     *
     * @return
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory().getAbsoluteFile();//获取跟目录
        }
        return sdDir.toString();
    }

    //    Bitmap对象保存味图片文件
    /** 保存方法 */
    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static void saveBitmapFile(Bitmap bm, String fileName) throws IOException {
        String path = getSDPath() +"/CollegeLife/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        String icon_path=path + fileName+".png";
        File myCaptureFile = new File(icon_path);
        SpUtils.getInstan().save(SpUtils.USER_ICON_PATH,icon_path);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }
}
