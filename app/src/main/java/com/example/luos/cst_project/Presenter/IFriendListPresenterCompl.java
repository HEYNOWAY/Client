package com.example.luos.cst_project.Presenter;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.MsgDbContract;
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.FriendListActivity;
import com.example.luos.cst_project.View.IFriendListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/7/28.
 */

public class IFriendListPresenterCompl extends BaseIPresenter{
    private static final String TAG = "IFriendListPresenter";
    private static IFriendListView iFriendListView;

    public IFriendListPresenterCompl(IFriendListView iFriendListView){
        this.iFriendListView = iFriendListView;
    }

    public void saveMessageToDb(List<DataFrame.PersonalMsg> msgList) {
        Log.i(TAG, "saveMessageToDb()....");
        ContentValues values = new ContentValues();
        for (DataFrame.PersonalMsg msg:msgList) {
            values.put(MsgDbContract.MsgEntry.SEND_ID,msg.getRecverID());
            values.put(MsgDbContract.MsgEntry.RECEVICE_ID,msg.getSenderID());
            values.put(MsgDbContract.MsgEntry.TYPE,msg.getMsgType());
            values.put(MsgDbContract.MsgEntry.CONTENT,msg.getContent());
            values.put(MsgDbContract.MsgEntry.TIME,msg.getSendTime());
            values.put(MsgDbContract.MsgEntry.DIRECTION,Config.MESSAGE_FROM);
            getDbUtil().insertMessage(values);
        }
    }


}
