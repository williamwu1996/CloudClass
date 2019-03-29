package com.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CHATHISTORY = "create table chathistory(" +
            "hid integer primary key autoincrement,"  +
            "sender varchar(32)," +
            "receiver varchar(32)," +
            "content varchar(300)," +
            "isread char(1))";

    public static final String CREATE_CHECKIN = "create table checkin(" +
            "chid int not null," +
            "uid int," +
            "uname varchar(32)," +
            "distance int" +
            ")" ;

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHATHISTORY);
        db.execSQL(CREATE_CHECKIN);
//        Toast.makeText(mContext,"Create db success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists chathistory");
        db.execSQL("drop table if exists checkin");
        onCreate(db);
    }
}
