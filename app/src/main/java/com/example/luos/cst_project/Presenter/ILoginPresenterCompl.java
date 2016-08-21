package com.example.luos.cst_project.Presenter;


import android.content.ContentValues;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;


import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.ILoginView;
import com.example.luos.cst_project.View.LoginActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/6/25.
 */

public class ILoginPresenterCompl implements ILoginPresenter {
    private static final String TAG = "ILoginPresenterCompl";
    private static ILoginView iLoginView;
    private NetWork netWork = NetWork.getInstance();
    private DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    private DataFrame.User.Builder userBuilder = DataFrame.User.newBuilder();


    public ILoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        netWork = NetWork.getInstance();
        netWork.start();
        netWork.addIPresenter(this);
        Log.i(TAG, "netWork.strat()....");
    }

    @Override
    public void doLogin(String username, String password) {
        Log.d("Test","new stuct...");
        DataFrame.Msg send_msg = msgBuilder
                .setUserOpt(Config.REQUEST_LOGIN)
                .setUser(
                        userBuilder
                                .setUesrName(username)
                                .setUserPwd(password)
                ).build();
        boolean success= netWork.writeToSrv(send_msg);
        if (!success) {
            iLoginView.makeToast("网络不可用...");
            iLoginView.setProgressbarVisible(View.INVISIBLE);
        }
    }

    @Override
    public void getOffMsg(int userId) {
        DataFrame.Msg send_msg = msgBuilder
                .setUserOpt(Config.REQUEST_GET_OFFLINE_MSG)
                .setUser(
                        userBuilder.setUserID(userId)
                ).build();
        boolean success = netWork.writeToSrv(send_msg);
        if (!success) {
            iLoginView.makeToast("获取离线消息失败...");
        }
    }

    @Override
    public void saveMessageToDb(List<DataFrame.PersonalMsg> msgList) {
        Log.i(TAG, "saveMessageToDb()....");
        ContentValues values = new ContentValues();
        for (DataFrame.PersonalMsg msg : msgList) {
            values.put(MsgEntry.SEND_ID, msg.getRecverID());
            values.put(MsgEntry.RECEVICE_ID, msg.getSenderID());
            values.put(MsgEntry.TYPE, msg.getMsgType());
            values.put(MsgEntry.CONTENT, msg.getContent());
            values.put(MsgEntry.TIME, msg.getSendTime());
            values.put(MsgEntry.DIRECTION, Config.MESSAGE_FROM);
            BaseActivity.getDbUtil().insertMessage(values);
        }
    }

    @Override
    public void onProcess(DataFrame.Msg recive_msg) {
        switch (recive_msg.getUserOpt()){
            case Config.RESULT_LOGIN:
                Log.i(TAG,"handlogin()... and result is:"+recive_msg.getOptResult());
                if(recive_msg.getOptResult()==true){
                    User user = new User();
                    String nickName = recive_msg.getUser().getNickName();
                    user.setNickName(nickName);
                    int userId = recive_msg.getUser().getUserID();
                    user.setUserID(userId);
                    String userName = recive_msg.getUser().getUesrName();
                    user.setUserName(userName);
                    BaseActivity.setSelf(user);
                    BaseActivity.sendEmptyMessage(Config.LOGIN_SUCCESS);
                    Log.i(TAG,"handlogin()...finished");

                } else {
                    BaseActivity.sendEmptyMessage(Config.LOGIN_FAILED);
                    Log.i(TAG,"handlogin()...failed,reason:"+recive_msg.getReceiveResult());
                }
                break;

            case Config.RESULT_GET_OFFLINE_MSG:
                Log.i(TAG,"handleGetoffLineMsg()...");
                Message message = Message.obtain();
                if(recive_msg.getOptResult()==true){
                    List<DataFrame.PersonalMsg> msgList = recive_msg.getPersonalMsgList();
                    ArrayList list = new ArrayList();
                    list.add(msgList);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("msgList",list);
                    message.what = Config.SEND_NOTIFICATION;
                    message.setData(bundle);
                    BaseActivity.sendMessage(message);
                    Log.i(TAG,"getOffmsg is:"+msgList);
                }
                break;
        }


    }
}
