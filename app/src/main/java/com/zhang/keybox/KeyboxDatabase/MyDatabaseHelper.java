package com.zhang.keybox.KeyboxDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 张 on 2017/2/1.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_KEY = "create table Key (" + "id integer primary key autoincrement," + "uuid text," + "name text," + "count text,"
            + "password text," + "remark text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
