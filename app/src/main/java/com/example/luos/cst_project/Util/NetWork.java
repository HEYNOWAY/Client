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
   private static DataFrame.Msg send_msg ;
   private static DataFrame.Msg recive_msg;
   Vector<ReceiveInfoListener> listeners=new Vector<ReceiveInfoListener>();



   public synchronized static NetWork getInstance(){
         if(instance==null){
            instance = new NetWork();
         }
         return instance;
   }

   @Override
   public void run() {
      while (onWork){
         switch (state){
            case connect :
               connect();
               break;
            case running:
               reciveMsg();
               break;
         }
      }
   }

   private  void connect(){

      try {
         Log.d("Test_socket","start connect");
         socket = new Socket(Contents.SERVER_ID,Contents.PORT);
         state = running;
         dos = new DataOutputStream(socket.getOutputStream());
         dis =new DataInputStream(socket.getInputStream());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public  void reciveMsg(){
            try {
               Log.d("Test_recivemsg","executd");
               byte[] buffer = new byte[1024*4];
               int len = dis.read(buffer);
               if(len!=-1){
                  String str = new String(buffer,0,len);
                  recive_msg =DataFrame.Msg.parseFrom(str.getBytes());
                  Log.d("Test_reciveMsg",recive_msg.toString());
               }
               int type = recive_msg.getUserOpt();
               switch (type){
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
               Log.d("Test_IOException",e.toString());
            }
   }


   /**
     *  登陆请求
     * @param username
     * @param password
     * @return
     */
   public boolean login(String username,String password){

      try {
           if(dos==null){
              return false;
           }
            Log.d("Test_net_login","excuted login!");
            send_msg = msgBuilder
                    .setUserOpt(Config.REQUEST_LOGIN)
                    .setUser(
                            userBuilder
                              .setUesrName(username)
                              .setUserPwd(password)
                    ).build();
            if(send_msg!=null){
               dos.write(send_msg.toByteArray());
               dos.flush();
               Log.d("Test_dos", "write:"+send_msg);
               return true;
            } else {
               return false;
            }
      } catch (IOException e) {
         e.printStackTrace();
         Log.d("Test_dos_Excepiton",  e.toString());
         return false;
      }
   }

   /**
    *  处理登陆结果
    */
   public void handlogin(){
         if(recive_msg.getOptResult()==true){
            User user = new User();
            String nickName = recive_msg.getUser().getNickName();
            user.setNickName(nickName);
            int userId = recive_msg.getUser().getUserID();
            user.setUserID(userId);
            String userName = recive_msg.getUser().getUesrName();
            user.setUserName(userName);
            BaseIPresenter.setUser(user);
            Log.d("Test_server_msg","User NickName Id:"+user.getNickName()+user.getUserID());
            BaseIPresenter.sendEmptyMessage(Config.LOGIN_SUCCESS);
            getFriends(userId);

         } else {
            BaseIPresenter.sendEmptyMessage(Config.LOGIN_FAILED);
         }

   }

   /**
    * 获取好友列表请求
    * @param userId
     */
   public void getFriends(int userId) {
      try {
         Log.d("Test_getFriends","start...getFriends");
         send_msg = msgBuilder
                 .setUserOpt(Config.REQUEST_GET_FRIENDS)
                 .setUser(
                         userBuilder.setUserID(userId)
                 ).build();
         dos.write(send_msg.toByteArray());
         Log.d("Test_getFriends",send_msg.toString());
         dos.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void handletfriend() {
      if(recive_msg.getOptResult()==true){
         List<DataFrame.User> friends = recive_msg.getFriendsList();
         ContentValues values=new ContentValues();
         int friendId;
         String nickName;
         for (DataFrame.User friend: friends) {

            friendId = friend.getUserID();
            nickName = friend.getNickName();

            values.put(FriendsEntry.USER_ID, BaseIPresenter.getUser().getUserID());
            values.put(FriendsEntry.FRIEND_ID, friendId);
            values.put(FriendsEntry.NICKNAME, nickName);

            BaseIPresenter.getDbUtil().insertFriend(values);
         }
      } else if(recive_msg.getOptResult()==false){
         //handle 返回界面处理
      }
   }

   /**
    * 发送文本信息
    * @param selfId
    * @param friendId
    * @param content
    * @param time
     * @return
     */
   public boolean sendText(int selfId, int friendId, String content, String time){
      Log.d("Test_sendText","sendText()"+selfId+" send to "+friendId+" content:"+content+" time:"+time);
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
      } catch (IOException e) {
         e.printStackTrace();
         result = false;
      }
      return result;
   }

   /**
    * 获取离线消息
    */
   public void getOffMsg(int userId){
      try {
         Log.d("Test_getOffMsg","start...getOffMsg");
         send_msg = msgBuilder
                 .setUserOpt(Config.REQUEST_GET_OFFLINE_MSG)
                 .setUser(
                         userBuilder.setUserID(userId)
                 ).build();
         dos.write(send_msg.toByteArray());
         Log.d("Test_getOffMsg",send_msg.toString());
         dos.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void handleGetOffLineMsg() {
      Log.d("Test_HandleMsg","strart handleGetOffLineMsg()....");
      if(recive_msg.getOptResult()==true){
         List<DataFrame.PersonalMsg> msgList = recive_msg.getPersonalMsgList();
         ArrayList list = new ArrayList();
         list.add(msgList);
         Message message = Message.obtain();
         Bundle bundle = new Bundle();
         bundle.putParcelableArrayList("msgList",list);
         message.what = Config.SEND_NOTIFICATION;
         message.setData(bundle);
         BaseIPresenter.sendMessage(message);
         Log.d("Test_HandleMsg", BaseActivity.getCurrentActivity().toString()+": "+message);

      }
   }


}
