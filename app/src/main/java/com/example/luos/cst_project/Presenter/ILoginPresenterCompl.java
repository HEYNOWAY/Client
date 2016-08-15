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
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.ILoginView;
import com.example.luos.cst_project.View.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/6/25.
 */

public class ILoginPresenterCompl extends BaseIPresenter implements ILoginPresenter {
    private static ILoginView iLoginView;
    private NetWork netWork;


    public ILoginPresenterCompl(ILoginView iLoginView){
        this.iLoginView = iLoginView;
        netWork = NetWork.getInstance();
        Log.d("Test_newWork","a network instance");
        netWork.start();
        Log.d("Test_newWork","start netWork");

    }


    @Override
    public void doLogin(String username, String password) {
        boolean isLogin = netWork.login(username,password);
        if(!isLogin){
           iLoginView.onToastResult(Config.LOGIN_FAILED);
            Log.d("Test_doLogin","Failed!");
        }

    }


    @Override
    public void getOffMsg(int userId) {
        netWork.getOffMsg(LoginActivity.self.getUserID());
    }


    public static void setFriendList(List<DataFrame.User> friends) {
        BaseActivity.friends = new ArrayList<>(friends);
        Log.d("Test_setFriendList....",BaseActivity.friends.toString());
    }

    public static void setUser(User user){
        BaseActivity.self = user;
        Log.d("Test_setUser",BaseActivity.self.getUserID()+"");
    }
}
