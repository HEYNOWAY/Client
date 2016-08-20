package com.example.luos.cst_project.Presenter;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.DbUtil;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.BaseActivity;

/**
 * Created by luos on 2016/8/13.
 */

public class BaseIPresenter implements IPresenter {
    private static final String TAG = "BaseIPresenter";
    protected NetWork netWork = NetWork.getInstance();
    protected DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    protected DataFrame.User.Builder userBuilder = DataFrame.User.newBuilder();

    public BaseIPresenter() {
        netWork.addIPresenter(this);
    }

    public static void sendEmptyMessage(int what) {
        BaseActivity.sendEmptyMessage(what);
        Log.i(TAG, "Activity is" + BaseActivity.getCurrentActivity());
        Log.i(TAG, "send Empty message is: " + what);
    }

    public static void sendMessage(Message msg) {
        BaseActivity.sendMessage(msg);
        Log.i(TAG, "Activity is" + BaseActivity.getCurrentActivity());
        Log.i(TAG, "send Empty message is: " + msg);
    }

    public static DbUtil getDbUtil() {
        return BaseActivity.getDbUtil();
    }

    public static User getUser() {
        return BaseActivity.self;
    }

    public static void setUser(User user){
        BaseActivity.self = user;
    }

    public void stopWork() {
        netWork.setOnWork(false);
    }

    public void setInstanceNull() {
        netWork.setInstanceNull();
    }

    public void exitRequest(int userId) {
        DataFrame.Msg send_msg = msgBuilder
                .setUserOpt(Config.REQUEST_EXIT)
                .setUser(
                        userBuilder.setUserID(userId)
                ).build();
         netWork.writeToSrv(send_msg);

    }

    @Override
    public void onProcess(DataFrame.Msg msg) {

    }
}
