package com.example.luos.cst_project.Util;

import com.example.luos.cst_project.Model.MsgData;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by luos on 2016/8/13.
 */

public class DbUtil extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Message.db";


    public DbUtil(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ MsgEntry.TABLE_NAME + "( " +
                MsgEntry.SEND_ID + " int," +
                MsgEntry.RECEVICE_ID + " int," +
                MsgEntry.TYPE + " int," +
                MsgEntry.CONTENT + " varchar2(240)," +
                MsgEntry.TIME + " varchar2(12)" +
                " )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<MsgData> queryMessages(String selfId, String friendId){
        ArrayList<MsgData> list=new ArrayList<MsgData>();
        SQLiteDatabase db=getReadableDatabase();
        String selection="self_Id=? and friend_Id=?";
        String[] selectionArgs=new String[]{"'"+selfId+"'","'"+friendId+"'"};
        Cursor cursor=db.query("message", null, selection, selectionArgs, null, null, "time");

        //如果游标为空（查找失败）或查到的信息数位0，返回null
        if(cursor==null || cursor.getCount()==0){
            return list;
        }
        Log.i("Test_DbUtil", "queryMessages() cursor.count="+cursor.getCount());

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            MsgData message=new MsgData();
            message.setSendId(cursor.getInt(cursor.getColumnIndex("self_Id")));
            message.setReceiveId(cursor.getInt(cursor.getColumnIndex("friend_Id")));
            message.setType(cursor.getInt(cursor.getColumnIndex("type")));
            message.setContent(cursor.getString(cursor.getColumnIndex("content")));
            message.setTime(cursor.getString(cursor.getColumnIndex("time")));
            list.add(message);
            cursor.moveToNext();
//			Log.i(TAG, "queryMessages()：查到一条消息");
        }

        cursor.close();
        db.close();
        return list;
    }

    public void insertMessage(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();

        //插入一条新消息
        db.insert(MsgEntry.TABLE_NAME, null, values);

        int type=values.getAsInteger("type");
        String content=values.getAsString("content");
        String time=values.getAsString("time");
        String self_Id=values.getAsString("self_Id");
        String friend_Id=values.getAsString("friend_Id");
        Log.i("Test_DbUtil", "InsertMessage type="+type+" content="+content+" time="+time+" self_If="+self_Id+" friend_Id="+friend_Id);

        //更新同好友最后的一条聊天消息的时间、内容、类型
//        ContentValues updates=new ContentValues();
//        updates.put("type", type);
//        updates.put("content", content);
//        updates.put("time", time);
//        String where=MsgEntry.SEND_ID +"="+self_Id+" and "+MsgEntry.RECEVICE_ID+"="+friend_Id;
//        int number=db.update("friend", updates, where, null);
//        Log.i("Test_DbUtil", "insertMessage update friend number="+number);
        db.close();
    }
}
