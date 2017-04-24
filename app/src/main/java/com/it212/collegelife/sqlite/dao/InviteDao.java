package com.it212.collegelife.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.it212.collegelife.domain.InvationInfo;
import com.it212.collegelife.sqlite.InviteDBHelp;
import com.it212.collegelife.sqlite.TB.InviteTB;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by imac on 2017/4/19.
 */

public class InviteDao {

    private final InviteDBHelp dbHelp;

    public InviteDao(Context context) {
        dbHelp = new InviteDBHelp(context);
    }

    public void add(InvationInfo invationInfo) {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        ContentValues valuse = new ContentValues();
        valuse.put(InviteTB.COL_USER_NAME, invationInfo.getUserName());
        valuse.put(InviteTB.COL_REASON, invationInfo.getReason());
        valuse.put(InviteTB.COL_STATUS, invationInfo.getStatus().ordinal());
        db.replace(InviteTB.TAB_NAME, null, valuse);
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        db.delete(InviteTB.TAB_NAME, InviteTB.COL_USER_NAME, new String[]{name});
        db.close();
    }

    public List<InvationInfo> getInvitions() {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        String sql = "select * from " + InviteTB.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<InvationInfo> infoList=null;
        if (cursor != null && cursor.moveToNext()) {
            InvationInfo info=new InvationInfo();
            info.setReason(cursor.getString(cursor.getColumnIndex(InviteTB.COL_REASON)));
            info.setUserName(cursor.getString(cursor.getColumnIndex(InviteTB.COL_USER_NAME)));
            infoList.add(info);
        }
        cursor.close();
        db.close();
        return infoList;
    }
}
