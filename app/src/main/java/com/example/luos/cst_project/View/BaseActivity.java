package com.example.luos.cst_project.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.DbUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基类，所有Activity继承这个基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static LinkedList<BaseActivity> queue = new LinkedList<BaseActivity>();
    public static ArrayList<DataFrame.User> friends;
    private static final String TAG="WoliaoBaseActivity";
    private static final String PREFERENCE_NAME="woliao.pre";
    private static final String USERNAME="userName";
    private static final String PWD = "password";
    private static DbUtil dbUtil;
    public  static User self = new User();
    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!queue.isEmpty()){
                Log.d("Test_baseActivity","start handleMessage,"+queue.getLast().toString());
                queue.getLast().processMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!queue.contains(this)){
            queue.add(this);
        }
        if(dbUtil==null){
            dbUtil=new DbUtil(this);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void finish() {
        super.finish();
        if(!queue.isEmpty()){
            queue.removeLast();
        }
    }


    public void makeTextShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void makeTextLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public static BaseActivity getCurrentActivity(){
        return queue.getLast();
    }

    public static void sendMessage(Message msg){
        handler.sendMessage(msg);
    }

    public static void sendEmptyMessage(int what){
        handler.sendEmptyMessage(what);
    }

    public abstract  void processMessage(Message msg);

    protected void setPreference(String userName, String pwd){
        SharedPreferences sp=getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(USERNAME, userName);
        editor.putString(PWD, pwd);
        editor.commit();
    }

    protected User getPreference(){
        User user=new User();
        SharedPreferences sp=getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        String userName=sp.getString(USERNAME, "");
        String pwd=sp.getString(PWD, "");
        user.setUserName(userName);
        user.setUserPassword(pwd);
        return user;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifycation(List<DataFrame.PersonalMsg> msgList) {
        NotificationManager nm = (NotificationManager) getCurrentActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getCurrentActivity(),ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getCurrentActivity(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder build = new Notification.Builder(getCurrentActivity())
                .setContentTitle("有通知")
                .setContentText("你有一条新消息")
                .setSmallIcon(R.drawable.notify_icon)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis());
        Notification notify = build.build();
        nm.notify(1,notify);

    }

    public void saveMessageToDb(List<DataFrame.PersonalMsg> msgList) {
        ContentValues values = new ContentValues();
        for (DataFrame.PersonalMsg msg:msgList) {
            values.put("sender_id",msg.getSenderID());
            values.put("reciver_id",msg.getRecverID());
            values.put("type",msg.getMsgType());
            values.put("content",msg.getContent());
            values.put("time",msg.getSendTime());
            dbUtil.insertMessage(values);
        }
    }

}
