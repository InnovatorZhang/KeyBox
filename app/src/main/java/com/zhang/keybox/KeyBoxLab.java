package com.zhang.keybox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhang.keybox.KeyboxDatabase.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 张 on 2017/2/1.
 */

public class KeyBoxLab {//创建单例

    private static KeyBoxLab sKeyBoxLab;

    private List<KeyBox> mKeyBoxes;

    private Context mContext;

    private SQLiteDatabase mSQLiteDatabase;

    public static KeyBoxLab getKeyBoxLab(Context context){
        if (sKeyBoxLab == null){
            sKeyBoxLab = new KeyBoxLab(context);
        }
        return sKeyBoxLab;
    }

    private KeyBoxLab(Context context){
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new MyDatabaseHelper(mContext,"KeyBox.db",null,1).getWritableDatabase();

    }

    public List<KeyBox> getKeyBoxes(){

        mKeyBoxes = new ArrayList<>();

        Cursor cursor = queryKeyBoxes(null,null);

        if(cursor.moveToFirst()) {
            do {
                KeyBox keyBox = getKey(cursor);
                mKeyBoxes.add(keyBox);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return mKeyBoxes;
    }

    public KeyBox getKeyBox(UUID uuid){
        Cursor cursor = queryKeyBoxes("uuid = ?",new String[]{uuid.toString()});

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
           return getKey(cursor);

        }finally {
            cursor.close();
        }
    }

    public KeyBox getKey(Cursor cursor){
        String uuidString = cursor.getString(cursor.getColumnIndex("uuid"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String count = cursor.getString(cursor.getColumnIndex("count"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String remark = cursor.getString(cursor.getColumnIndex("remark"));

        KeyBox keyBox = new KeyBox(UUID.fromString(uuidString));

        keyBox.setName(name);
        keyBox.setCount(count);
        keyBox.setPassword(password);
        keyBox.setRemark(remark);

        return keyBox;
    }

    public void addKeyBox(KeyBox keyBox){

        ContentValues values = getContentValues(keyBox);

        mSQLiteDatabase.insert("Key",null,values);
    }

    public void updateKeyBox(KeyBox keyBox){
        String uuidString = keyBox.getId().toString();
        ContentValues values = getContentValues(keyBox);

        mSQLiteDatabase.update("Key",values,"uuid = ?",new String []{ uuidString });
    }

    public void deleteKeyBox(UUID uuid){
        mSQLiteDatabase.delete("Key","uuid = ?",new String[]{uuid.toString()});
    }

    private Cursor queryKeyBoxes(String whereClause,String[] whereArgs){

        Cursor cursor = mSQLiteDatabase.query("Key",null,whereClause,whereArgs,null,null,null);

        return cursor;
    }

    private static ContentValues getContentValues(KeyBox keyBox){

        ContentValues values = new ContentValues();
        values.put("uuid",keyBox.getId().toString());
        values.put("name",keyBox.getName());
        values.put("count",keyBox.getCount());
        values.put("password",keyBox.getPassword());
        values.put("remark",keyBox.getRemark());

        return values;
    }
}
