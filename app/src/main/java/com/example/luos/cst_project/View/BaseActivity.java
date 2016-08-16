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
import com.example.luos.cst_project.Model.Friend;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.DbUtil;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;

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

    protected static DbUtil dbUtil;
    public  static User self = new User();
    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!queue.isEmpty()){
                Log.i(TAG,"start handleMessage,Activity is "+queue.getLast().toString());
                queue.getLast().processMessage(msg);
            }
        }
    };

    public abstract  void processMessage(Message msg);

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


    public static User getSelf() {
        return self;
    }

    public static void setSelf(User self) {
        BaseActivity.self = self;
    }



    public void makeTextShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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

    public static DbUtil getDbUtil(){
        return dbUtil;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed().... Activity number="+queue.size());
        if(queue.size()==1){	//当前Activity是最后一个Activity了
//            showDialog(EXIT_DIALOG);
        }else{
            queue.getLast().finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifycation(List<DataFrame.PersonalMsg> msgList) {
        NotificationManager nm = (NotificationManager) getCurrentActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getCurrentActivity(),ChatActivity.class);
        if(msgList!=null){
            int id = 1;
            int currentSender = 0;
            for (DataFrame.PersonalMsg msg: msgList) {
                if( currentSender != msg.getSenderID()){
                    currentSender = msg.getSenderID();
                    Friend friend = dbUtil.queryFriend(msg.getSenderID()+"");
                    intent.putExtra("friend",friend);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getCurrentActivity(),id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification.Builder build = new Notification.Builder(getCurrentActivity())
                            .setContentTitle(friend.getFriendName())
                            .setContentText(msg.getContent())
                            .setSmallIcon(R.drawable.ic_notifications_white_36dp)
                            .setContentIntent(pendingIntent)
                            .setWhen(System.currentTimeMillis());
                    Notification notify = build.build();
                    notify.flags = Notification.FLAG_AUTO_CANCEL;
                    nm.notify(id,notify);
                    id++;
                }
            }
        }

    }

}
