package com.it212.collegelife.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;


/**
 * Created by imac on 2017/4/5.
 */

public class ImageDao {

    private SQLiteOpenHelper ImageHP;

    public ImageDao(Context context) {

    }
    /**
     * Bitmap.CompressFormat.JPEG 和 Bitmap.CompressFormat.PNG
     * JPEG 与 PNG 的是区别在于 JPEG是有损数据图像，PNG使用从LZ77派生的无损数据压缩算法。
     * 这里建议使用PNG格式保存
     * 100 表示的是质量为100%。当然，也可以改变成你所需要的百分比质量。
     * os 是定义的字节输出流
     *
     * .compress() 方法是将Bitmap压缩成指定格式和质量的输出流
     */
    public void saveImage(String id, Bitmap bitmap) {
        SQLiteDatabase database = ImageHP.getWritableDatabase();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        ContentValues value = new ContentValues();
//        value.put(UserInfo.COL_ID, id);
//        value.put(UserInfo.COL_CONTENT, os.toByteArray());
//        database.replace(UserInfo.TAB_NAME, null, value);
        database.close();
    }

//    public Bitmap getImage(String id){
//        SQLiteDatabase database = ImageHP.getWritableDatabase();
//        Bitmap bmpout =null;
////        String sql="select * from "+ UserInfo.TAB_NAME+" where "+ UserInfo.COL_ID+"=?";
////        Cursor cursor=database.rawQuery(sql, new String[]{id});
//        if (cursor.moveToNext()){
//            byte[] in =cursor.getBlob(1);
//            /**
//             * 根据Bitmap字节数据转换成 Bitmap对象
//             * BitmapFactory.decodeByteArray() 方法对字节数据，从0到字节的长进行解码，生成Bitmap对像。
//             **/
//           bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
//        }
//        cursor.close();
//        database.close();
//        return bmpout;
//    }
}
