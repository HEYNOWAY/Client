package com.example.luos.cst_project.Presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.FriendListActivity;
import com.example.luos.cst_project.View.IFriendListView;

import java.util.ArrayList;

/**
 * Created by luos on 2016/7/28.
 */

public class IFriendListPresenterCompl extends BaseIPresenter{
    private static IFriendListView iFriendListView;

    public IFriendListPresenterCompl(IFriendListView iFriendListView){
        this.iFriendListView = iFriendListView;
    }




}
