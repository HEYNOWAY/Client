package com.example.luos.cst_project.Presenter;

import android.os.Message;
import android.util.Log;

import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.DbUtil;
import com.example.luos.cst_project.View.BaseActivity;

/**
 * Created by luos on 2016/8/13.
 */

public class BaseIPresenter {

    public static void sendEmptyMessage(int what) {
        BaseActivity.sendEmptyMessage(what);
    }

    public static void sendMessage(Message msg){
        BaseActivity.sendMessage(msg);
    }

    public static DbUtil getDbUtil(){
        return BaseActivity.getDbUtil();
    }

    public static User getUser(){
        return BaseActivity.getSelf();
    }

    public static void setUser(User user){
        BaseActivity.setSelf(user);
    }

}
