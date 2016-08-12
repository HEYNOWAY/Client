package com.example.luos.cst_project.View;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 基类，所有Activity继承这个基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static LinkedList<BaseActivity> queue = new LinkedList<BaseActivity>();
    public static ArrayList<DataFrame.User> friends;
    private static final String TAG="WoliaoBaseActivity";

    private static final String PREFERENCE_NAME="woliao.pre";
    private static final String USERNAME="userName";
    private static final String PWD="pwd";
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

}
