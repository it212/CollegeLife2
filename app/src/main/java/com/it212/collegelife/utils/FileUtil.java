package com.it212.collegelife.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by imac on 2017/4/20.
 */

public class FileUtil {
    public static final String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH = "congmingtou";

    /**
     * 是否支持SDCard
     */
    public static boolean isSupportSDCard() {
        return Environment.getExternalStorageDirectory().exists();
    }

    /**
     * 检测文件或者路径是否存在
     * <p>
     * 可以给值为Null，如果给值null,判断路径是否存在
     */

    public static boolean isExists(String path, String fileName) {
        if (null == path && null == fileName) {
            return false;
        }
        String name;
        name = SDPATH + File.separator + path;
        File file = new File(name);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileNmae = new File(name, fileName);
        return fileNmae.exists();
    }

    public static boolean isExists(String path) {
        if (null == path) {
            return false;
        }
        String name;

        name = SDPATH + File.separator + path;

        File file = new File(name);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.exists();
    }
    /***
     *保存bitmap对象到文件中
     * @param bm
     * @param quality
     * @return
     */
    public static File saveBitmap2File(Bitmap bm, String name, int quality) {
        if (null == bm || bm.isRecycled()) {
            return null;
        }
        try {
            File dirFile  = new File(FileUtil.SDPATH+"/CollegeLifeIcon/");  //目录转化成文件夹
            if (!dirFile .exists()) {              //如果不存在，那就建立这个文件夹
                dirFile .mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            File file = new File(dirFile, name);// 在SDcard的目录下创建图片文,以当前时间为其命
            FileOutputStream bos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != bm) {
                if (!bm.isRecycled()) {
                    bm.recycle();
                }
                bm = null;
            }
        }
    }

}