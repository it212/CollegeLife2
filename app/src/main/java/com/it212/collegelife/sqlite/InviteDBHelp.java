package com.it212.collegelife.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.it212.collegelife.sqlite.TB.InviteTB;

/**
 * Created by imac on 2017/4/19.
 */

public class InviteDBHelp extends SQLiteOpenHelper {
    public InviteDBHelp(Context context){
        this(context,"InviteDB",null,1);
    }
    public InviteDBHelp(Context context,String name){
        this(context,name,null,1);
    }
    public InviteDBHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InviteTB.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
