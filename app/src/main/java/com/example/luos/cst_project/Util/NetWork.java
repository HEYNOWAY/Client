package com.example.luos.cst_project.Util;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.Contents;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.FriendDbContract.FriendsEntry;
import com.example.luos.cst_project.Presenter.BaseIPresenter;
import com.example.luos.cst_project.Presenter.ReceiveInfoListener;
import com.example.luos.cst_project.View.BaseActivity;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by luos on 2016/6/25.
 */

public class NetWork extends Thread {
    private static final String TAG = "NetWork";
    private final int connect = 1;
    private final int running = 2;
    private int state = connect;
    private static NetWork instance;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean onWork = true;
    private DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    private DataFrame.User.Builder userBuilder = DataFrame.User.newBuilder();
    private DataFrame.PersonalMsg.Builder personalBuilder = DataFrame.PersonalMsg.newBuilder();
    private static DataFrame.Msg send_msg;
    private static DataFrame.Msg recive_msg;
    Vector<ReceiveInfoListener> listeners = new Vector<ReceiveInfoListener>();


    public synchronized static NetWork getInstance() {
        Log.i(TAG, "getInstance()....");
        if (instance == null) {
            instance = new NetWork();
        }
        return instance;
    }

    @Override
    public void run() {
        while (onWork) {
            switch (state) {
                case connect:
                    connect();
                    break;
                case running:
                    reciveMsg();
                    break;
            }
        }
    }


    //程序退出时的清理工作
    public void setOnWork(boolean flag) {
        onWork = flag;
    }

    public void setInstanceNull() {
        instance = null;
    }

    private void connect() {

        try {
            Log.i(TAG, "connect()...start connected");
            socket = new Socket(Contents.SERVER_ID, Contents.PORT);
            state = running;
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void reciveMsg() {
        try {
            Log.i(TAG, "reciveMsg()...start recive messages");
            byte[] buffer = new byte[1024 * 4];
            int len = dis.read(buffer);
            if (len != -1) {
                String str = new String(buffer, 0, len);
                recive_msg = DataFrame.Msg.parseFrom(str.getBytes());
            }
            int type = recive_msg.getUserOpt();
            switch (type) {
                case Config.RESULT_LOGIN:
                    handlogin();
                    break;
                case Config.RESULT_GET_FRIENDS:
                    handletfriend();
                    break;
                case Config.RESULT_GET_OFFLINE_MSG:
                    handleGetOffLineMsg();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }


    /**
     * 登陆请求
     *
     * @param username
     * @param password
     * @return
     */
    public boolean login(String username, String password) {

        try {
            if (dos == null) {
                Log.i(TAG, "login request failed...");
                return false;
            }
            send_msg = msgBuilder
                    .setUserOpt(Config.REQUEST_LOGIN)
                    .setUser(
                            userBuilder
                                    .setUesrName(username)
                                    .setUserPwd(password)
                    ).build();
            if (send_msg != null) {
                dos.write(send_msg.toByteArray());
                dos.flush();
                Log.i(TAG, "write to server:" + send_msg);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, e.toString());
            return false;
        }
    }

    /**
     * 处理登陆结果
     */
    public void handlogin() {
        Log.i(TAG, "handlogin()... and result is:" + recive_msg.getOptResult());
        if (recive_msg.getOptResult() == true) {
            User user = new User();
            String nickName = recive_msg.getUser().getNickName();
            user.setNickName(nickName);
            int userId = recive_msg.getUser().getUserID();
            user.setUserID(userId);
            String userName = recive_msg.getUser().getUesrName();
            user.setUserName(userName);

            BaseIPresenter.setUser(user);
            BaseIPresenter.sendEmptyMessage(Config.LOGIN_SUCCESS);
            getFriends(userId);
            Log.i(TAG, "handlogin()...finished");

        } else {
            BaseIPresenter.sendEmptyMessage(Config.LOGIN_FAILED);
            Log.i(TAG, "handlogin()...failed,reason:" + recive_msg.getReceiveResult());
        }

    }

    /**
     * 获取好友列表请求
     *
     * @param userId
     */
    public void getFriends(int userId) {
        try {
            Log.i(TAG, "getFriends()...");
            send_msg = msgBuilder
                    .setUserOpt(Config.REQUEST_GET_FRIENDS)
                    .setUser(
                            userBuilder.setUserID(userId)
                    ).build();
            dos.write(send_msg.toByteArray());
            Log.i(TAG, "write to server:" + send_msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void handletfriend() {
        Log.i(TAG, "handlefriend()... and result is:" + recive_msg.getOptResult());
        if (recive_msg.getOptResult() == true) {
            List<DataFrame.User> friends = recive_msg.getFriendsList();
            ContentValues values = new ContentValues();
            int friendId;
            String nickName;
            for (DataFrame.User friend : friends) {

                friendId = friend.getUserID();
                nickName = friend.getNickName();

                values.put(FriendsEntry.USER_ID, BaseIPresenter.getUser().getUserID());
                values.put(FriendsEntry.FRIEND_ID, friendId);
                values.put(FriendsEntry.NICKNAME, nickName);

//            BaseIPresenter.getDbUtil().insertFriend(values);
            }
        } else if (recive_msg.getOptResult() == false) {
            //handle 返回界面处理
        }
    }

    /**
     * 发送文本信息
     *
     * @param selfId
     * @param friendId
     * @param content
     * @param time
     * @return
     */
    public boolean sendText(int selfId, int friendId, String content, String time) {
        Log.i(TAG, "sendText()...");
        boolean result = true;
        try {
            send_msg = msgBuilder.setUserOpt(Config.REQUEST_SEND_TXT)
                    .addPersonalMsg(
                            personalBuilder.setSenderID(selfId)
                                    .setRecverID(friendId)
                                    .setContent(content)
                                    .setSendTime(time)).build();
            dos.write(send_msg.toByteArray());
            dos.flush();
            Log.i(TAG, "write to server:" + send_msg);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
            Log.d(TAG, e.toString());
        }
        return result;
    }

    /**
     * 获取离线消息
     */
    public void getOffMsg(int userId) {
        try {
            Log.i(TAG, "getOffMsg()...");
            send_msg = msgBuilder
                    .setUserOpt(Config.REQUEST_GET_OFFLINE_MSG)
                    .setUser(
                            userBuilder.setUserID(userId)
                    ).build();
            dos.write(send_msg.toByteArray());
            Log.i(TAG, "write to server:" + send_msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void handleGetOffLineMsg() {
        Log.i(TAG, "handleGetoffLineMsg()...");
        Message message = Message.obtain();
        if (recive_msg.getOptResult() == true) {
            List<DataFrame.PersonalMsg> msgList = recive_msg.getPersonalMsgList();
            ArrayList list = new ArrayList();
            list.add(msgList);

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("msgList", list);
            message.what = Config.SEND_NOTIFICATION;
            message.setData(bundle);

            BaseIPresenter.sendMessage(message);
            Log.i(TAG, "current activity :" + BaseActivity.getCurrentActivity());
            Log.i(TAG, "getOffmsg is:" + msgList);
        } else {
            message.what = Config.SEND_NOTIFICATION;
        }
    }

    public void sendExitRequest(int userId) {
        try {
            Log.i(TAG, "getOffMsg()...");
            send_msg = msgBuilder
                    .setUserOpt(Config.REQUEST_EXIT)
                    .setUser(
                            userBuilder.setUserID(userId)
                    ).build();
            dos.write(send_msg.toByteArray());
            Log.i(TAG, "write to server:" + send_msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }


}
