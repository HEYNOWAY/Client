package com.example.luos.cst_project.Util;

import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Presenter.IPresenter;
import com.google.protobuf.InvalidProtocolBufferException;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by luos on 2016/6/25.
 */

public class NetWork extends Thread {
   private static final String TAG = "NetWork";
   private static NetWork instance;
   private Socket socket;
   private DataInputStream dis;
   private DataOutputStream dos;
   private boolean isConnected;
   private List<IPresenter> mPresenterList = new ArrayList<>();
   private BlockingDeque<DataFrame.Msg> dataQueue= new LinkedBlockingDeque<>();
   private DataFrame.Msg recive_msg;


   public synchronized static NetWork getInstance(){
      Log.i(TAG,"getInstance()....");
      if(instance==null){
         instance = new NetWork();
      }
      return instance;
   }

   @Override
   public void run() {
      connect();
   }

   private  void connect(){
      try {
         Log.i(TAG,"connect()...start connected");
         socket = new Socket();
         socket.connect(new InetSocketAddress(Config.SERVER_ID,Config.PORT),5000);
         if(socket.isConnected()){
            dos = new DataOutputStream(socket.getOutputStream());
            dis =new DataInputStream(socket.getInputStream());
            isConnected = true;
            handleData();
            read();
         } else {
            isConnected = false;
         }
      } catch (IOException e) {
         e.printStackTrace();
         Log.d(TAG,e.toString());
      }
   }

   private void handleData() {
      new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  DataFrame.Msg msg = dataQueue.take();
                  for(IPresenter presenter : mPresenterList)
                     presenter.onProcess(msg);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
         }
      }).start();
   }


   public  void read(){
      while (isConnected) {
         try {
            Log.i(TAG, "reciveMsg()...start recive messages");
            byte[] buffer = new byte[1024 * 4];
            int len = dis.read(buffer);
            if (len != -1) {
               String str = new String(buffer, 0, len);
               recive_msg = DataFrame.Msg.parseFrom(str.getBytes());
               dataQueue.put(recive_msg);
            }
         } catch (InterruptedException e1) {
            e1.printStackTrace();
         } catch (InvalidProtocolBufferException e1) {
            e1.printStackTrace();
         } catch (IOException e1) {
            e1.printStackTrace();
            Log.d(TAG, e1.toString());
         }
      }
   }

   public boolean writeToSrv(DataFrame.Msg msg){
      if(dos!=null){
         try {
            dos.write(msg.toByteArray());
            dos.flush();
            Log.i(TAG,"write to server:"+msg);
            return true;
         } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG,e.toString());
            return false;
         }
      }
      Log.i(TAG,"目前没有连接上服务器");
      return false;
   }

   public void addIPresenter(IPresenter presenter){
      Log.i(TAG,presenter+"插入");
      mPresenterList.add(presenter);
   }

   //程序退出时的清理工作
   public void setOnWork(boolean flag){
      isConnected = flag;
   }

   public void setInstanceNull(){
      instance=null;
   }

}
