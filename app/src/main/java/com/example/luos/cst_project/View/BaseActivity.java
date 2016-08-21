package com.example.luos.cst_project.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.luos.cst_project.Presenter.BaseIPresenter;
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

    private static final String TAG="BaseActivity";
    private final int EXIT_DIALOG=0x12;
    protected static DbUtil dbUtil;
    public  static User self = new User();
    private static LinkedList<BaseActivity> queue = new LinkedList<>();
    private BaseIPresenter iPresenter = new BaseIPresenter();
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
            Log.i(TAG,"acitvity add:"+this);
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
            BaseActivity activity = queue.removeLast();
            Log.d(TAG,"acitvity remove:"+activity);
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
        if(!queue.isEmpty())
            return queue.getLast();
        else
            return null;
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
            showDialog(EXIT_DIALOG);
        }else{
            queue.getLast().finish();
        }
    }

    public void exit() {
        //关闭Socket连接、输入输出流
       iPresenter.stopWork();
        iPresenter.setInstanceNull();

        //关闭数据库、MediaPlayer
        if(dbUtil!=null){
            dbUtil=null;
        }

        //销毁Activity
        while (queue.size() > 0){
            Log.d(TAG,"acitvity remove:"+this);
            queue.getLast().finish();
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        Log.i(TAG, "dialog id="+id);
        switch(id){
            case EXIT_DIALOG:{
                Log.i(TAG, "要弹出的是退出提醒对话框");
                builder.setMessage("真的要退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //向服务器发送“退出”请求
                                iPresenter.exitRequest(self.getUserID());

                                //关闭到服务器的Socket连接，输入流、输出流
                                exit();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
            break;
        }
        AlertDialog dialog=builder.create();
        Log.i(TAG, "dialog="+dialog);
        return dialog;
    }

    private void showExitDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("确定退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //向服务器发送“退出”请求
                        iPresenter.exitRequest(self.getUserID());

                        //关闭到服务器的Socket连接，输入流、输出流
                        exit();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
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
