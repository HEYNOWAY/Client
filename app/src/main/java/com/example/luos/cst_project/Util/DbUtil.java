package com.example.luos.cst_project.Util;

import com.example.luos.cst_project.Model.ChatMessage;
import com.example.luos.cst_project.Model.Friend;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;
import com.example.luos.cst_project.Util.FriendDbContract.FriendsEntry;

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
    public static final int DATABASE_VERSION = 28;
    public static final String DATABASE_NAME = "Message.db";
    private static final String TAG = "DbUtil";
    private static final String SQL_DELETE_FRIENDS =
            "DROP TABLE IF EXISTS " + FriendsEntry.TABLE_NAME;
    private static final String SQL_DELETE_MSG =
            "DROP TABLE IF EXISTS " + MsgEntry.TABLE_NAME;
    public DbUtil(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG,"onCreate()....");
        String sql = "create table "+ MsgEntry.TABLE_NAME + "( " +
                MsgEntry._ID + " INTEGER PRIMARY KEY,"+
                MsgEntry.SEND_ID + " integer," +
                MsgEntry.RECEVICE_ID + " integer," +
                MsgEntry.DIRECTION + " integer," +
                MsgEntry.TYPE + " integer," +
                MsgEntry.CONTENT + " varchar2(240)," +
                MsgEntry.TIME + " varchar2(12)" +
                " )";
        db.execSQL(sql);

        String userSql="create table " + FriendsEntry.TABLE_NAME + "( " +
                FriendsEntry._ID + " INTEGER PRIMARY KEY,"+
                FriendsEntry.USER_ID + " integer," +
                FriendsEntry.FRIEND_ID + " integer," +
                FriendsEntry.NICKNAME + " varchar2(20),"+
                FriendsEntry.TYPE + " integer,"+
                FriendsEntry.CONTENT + " varchar(240)," +
                FriendsEntry.TIME + " varchar2(12)"+
                " )";
        db.execSQL(userSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"onUpgrade()...version is:"+newVersion);
        db.execSQL(SQL_DELETE_FRIENDS);
        db.execSQL(SQL_DELETE_MSG);
        onCreate(db);
    }

    public ArrayList<ChatMessage> queryMessages(String selfId, String friendId){
        Log.i(TAG,"queryMessage()...");
        ArrayList<ChatMessage> list=new ArrayList<ChatMessage>();
        SQLiteDatabase db=getReadableDatabase();
        String selection=MsgEntry.SEND_ID +"=? and "+ MsgEntry.RECEVICE_ID +"=?";
        String[] selectionArgs=new String[]{selfId,friendId};

        Cursor cursor=db.query(MsgEntry.TABLE_NAME,null,selection,selectionArgs,null,null,MsgEntry.TIME);
        Log.i(TAG, "queryMessages() cursor.count="+cursor.getCount());
        //如果游标为空（查找失败）或查到的信息数位0，返回null
        if(cursor==null || cursor.getCount()==0){
            Log.i(TAG, "DatabaseUtil queryMessage() 异常：游标为空");
            return list;
        }


        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ChatMessage message=new ChatMessage();
            message.setSendId(cursor.getInt(cursor.getColumnIndex(MsgEntry.SEND_ID)));
            message.setReceiveId(cursor.getInt(cursor.getColumnIndex(MsgEntry.RECEVICE_ID)));
            message.setType(cursor.getInt(cursor.getColumnIndex(MsgEntry.TYPE)));
            message.setContent(cursor.getString(cursor.getColumnIndex(MsgEntry.CONTENT)));
            message.setTime(cursor.getString(cursor.getColumnIndex(MsgEntry.TIME)));
            message.setDirection(cursor.getInt(cursor.getColumnIndex(MsgEntry.DIRECTION)));
            list.add(message);
            cursor.moveToNext();
			Log.i(TAG, "queryMessages()：查到一条消息"+message);
        }

        cursor.close();
        db.close();
        return list;
    }

    public void insertMessage(ContentValues values){
        Log.i(TAG,"insertMessage()...");
        SQLiteDatabase db=getWritableDatabase();

        //插入一条新消息
        long num = db.insert(MsgEntry.TABLE_NAME, null, values);
        Log.i(TAG,"number is:"+num);
        int type=values.getAsInteger(MsgEntry.TYPE);
        int direction = values.getAsInteger(MsgEntry.DIRECTION);
        String content=values.getAsString(MsgEntry.CONTENT);
        String time=values.getAsString(MsgEntry.TIME);
        String self_Id=values.getAsString(MsgEntry.SEND_ID);
        String friend_Id=values.getAsString(MsgEntry.RECEVICE_ID);

        Log.i(TAG, "Insert Message type="+type+" direction="+direction+" content="+content+" time="+time+" self_If="+self_Id+" friend_Id="+friend_Id);

        //更新同好友最后的一条聊天消息的时间、内容、类型
        ContentValues updates=new ContentValues();
        updates.put(MsgEntry.TYPE, type);
        updates.put(MsgEntry.CONTENT, content);
        updates.put(MsgEntry.TIME, time);
        String where=FriendsEntry.USER_ID +"="+self_Id+" and "+FriendsEntry.FRIEND_ID+"="+friend_Id;
        int number=db.update(FriendsEntry.TABLE_NAME, updates, where, null);
        Log.i(TAG, "insertMessage update friend number:"+number);
        db.close();
    }

    // 插入一个好友信息
    public void insertFriend(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        long id=db.insert(FriendsEntry.TABLE_NAME, null, values);
        if(id!=-1){
            Log.i("Test_DbUtil", "向friend表插入"+values.get(FriendsEntry.FRIEND_ID)+"成功");
        }
        db.close();
    }

    //查询自己的所有好友信息
    public ArrayList<Friend> queryFriends(String selfId){
        Log.i(TAG,"queryFriends");
        ArrayList<Friend> list=new ArrayList<Friend>();
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={FriendsEntry.FRIEND_ID,FriendsEntry.NICKNAME,FriendsEntry.TYPE,FriendsEntry.CONTENT,FriendsEntry.TIME};
        String selection=FriendsEntry.USER_ID+"="+selfId+" and "+FriendsEntry.FRIEND_ID+"!="+selfId;
        Cursor cursor=db.query(FriendsEntry.TABLE_NAME, columns, selection, null, null, null, null);

        //如果游标为空（查找失败）或查到的信息数位0，返回null
        if(cursor==null || cursor.getCount()==0){
            db.close();
            Log.i(TAG, "DatabaseUtil queryFriends() 异常：游标为空");
            return list;
        }
		Log.i(TAG, "queryFriends() cursor.count="+cursor.getCount());
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Friend friend=new Friend();
            friend.setFriendID(cursor.getInt(cursor.getColumnIndex(FriendsEntry.FRIEND_ID)));
            friend.setFriendName(cursor.getString(cursor.getColumnIndex(FriendsEntry.NICKNAME)));
            friend.setType(cursor.getInt(cursor.getColumnIndex(FriendsEntry.TYPE)));
            friend.setContent(cursor.getString(cursor.getColumnIndex(FriendsEntry.CONTENT)));
            friend.setTime(cursor.getString(cursor.getColumnIndex(FriendsEntry.TIME)));

            list.add(friend);
            cursor.moveToNext();
        }
        db.close();
        return list;
    }

    public Friend queryFriend(String friendId){
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={FriendsEntry.FRIEND_ID,FriendsEntry.NICKNAME};
        String selection=FriendsEntry.FRIEND_ID+"='"+friendId+"'";
        Cursor cursor=db.query(FriendsEntry.TABLE_NAME, columns, selection, null, null, null, null);

        //如果游标为空（查找失败）或查到的信息数位0，返回null
        if(cursor==null || cursor.getCount()==0){
            db.close();
            Log.i(TAG, "DatabaseUtil queryFriends() 异常：游标为空");
            return null;
        }

        cursor.moveToFirst();
        Friend friend=new Friend();
        if(!cursor.isAfterLast()){
            friend.setFriendID(cursor.getInt(cursor.getColumnIndex(FriendsEntry.FRIEND_ID)));
            friend.setFriendName(cursor.getString(cursor.getColumnIndex(FriendsEntry.NICKNAME)));
        }
        db.close();

        return friend;
    }
}
