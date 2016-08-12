package com.example.luos.cst_project.Presenter;

//import android.content.Context;
//import android.content.Intent;
import android.os.Message;
//import android.text.LoginFilter;
import android.util.Log;
//import android.view.View;
//import android.widget.Toast;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/6/25.
 */

public class ILoginPresenterCompl implements ILoginPresenter {
    private static LoginActivity loginActivity;
    private NetWork netWork;


    public ILoginPresenterCompl(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
        netWork = NetWork.getInstance();
        Log.d("Test_newWork","a network instance");
        netWork.start();
        Log.d("Test_newWork","start netWork");

    }

    public static void sendEmptyMessage(int what) {
        Log.d("Test_sendEmptyMessage","start...");
        loginActivity.sendEmptyMessage(what);
    }

    public static void sendMessage(Message msg){
        loginActivity.sendMessage(msg);
    }

    @Override
    public void doLogin(String username, String password) {
        boolean isLogin = netWork.login(username,password);
        if(!isLogin){
            loginActivity.onToastResult(Config.LOGIN_FAILED);
            Log.d("Test_doLogin","Failed!");
        }

    }


    @Override
    public void setProgressBarVisible(int visibility) {
       loginActivity.setProgressbarVisible(visibility);
    }

    @Override
    public void getOffMsg() {
        netWork.getOffMsg();
    }


    public static void setFriendList(List<DataFrame.User> friends) {

        loginActivity.friends = new ArrayList<>(friends);
        Log.d("Test_setFriendList....",loginActivity.friends.toString());
    }

    public static void setUser(User user){
        loginActivity.self = user;
        Log.d("Test_setUser",loginActivity.self.getUserID()+"");
    }
}
