package com.example.luos.cst_project.Presenter;

import android.content.ContentValues;
import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.Friend;
import com.example.luos.cst_project.Util.DbUtil;
import com.example.luos.cst_project.Util.MsgDbContract;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.IFriendListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/7/28.
 */


public class IFriendListPresenterCompl implements IFriendListPresenter{

    private static final String TAG = "IFriendListPresenter";
    private IFriendListView iFriendListView;
    private DbUtil dbUtil;
    private NetWork netWork = NetWork.getInstance();

    public IFriendListPresenterCompl(IFriendListView iFriendListView){
        this.iFriendListView = iFriendListView;
        dbUtil = BaseActivity.getDbUtil();
        netWork.addIPresenter(this);
    }

    public void saveMessageToDb(List<DataFrame.PersonalMsg> msgList) {
        Log.i(TAG, "saveMessageToDb()....");
        ContentValues values = new ContentValues();

        for (DataFrame.PersonalMsg msg : msgList) {
            values.put(MsgDbContract.MsgEntry.SEND_ID, msg.getRecverID());
            values.put(MsgDbContract.MsgEntry.RECEVICE_ID, msg.getSenderID());
            values.put(MsgDbContract.MsgEntry.TYPE, msg.getMsgType());
            values.put(MsgDbContract.MsgEntry.CONTENT, msg.getContent());
            values.put(MsgDbContract.MsgEntry.TIME, msg.getSendTime());
            values.put(MsgDbContract.MsgEntry.DIRECTION, Config.MESSAGE_FROM);
            dbUtil.insertMessage(values);
        }
    }

    @Override
    public ArrayList<Friend> getFriendsFromDb() {
        int userId = BaseActivity.self.getUserID();
        return dbUtil.queryFriends(userId+"");
    }


    @Override
    public void onProcess(DataFrame.Msg msg) {

    }



}
