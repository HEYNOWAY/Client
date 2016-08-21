package com.example.luos.cst_project.Util;

import android.content.Context;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
    private BlockingDeque<DataFrame.Msg> dataQueue = new LinkedBlockingDeque<>();
    private DataFrame.Msg recive_msg;

    private final String ConstHeader = "142857";
    private final int ConstHeaderLength = 6;
    private final int ConstMsgLength = 4;


    public DataFrame.Msg getMsg(){
        return recive_msg;
    }
    public synchronized static NetWork getInstance() {
        Log.i(TAG, "getInstance()....");
        if (instance == null) {
            instance = new NetWork();
        }
        return instance;
    }

    @Override
    public void run() {
        connect();
    }

    private void connect() {
        try {
            Log.i(TAG, "connect()...start connected");
            socket = new Socket();
            socket.connect(new InetSocketAddress(Config.SERVER_ID, Config.PORT), 5000);
            if (socket.isConnected()) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                isConnected = true;
                handleData();
                read();
            } else {
                isConnected = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }


    private void handleData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataFrame.Msg msg = dataQueue.take();
                        for (IPresenter presenter : mPresenterList)
                            presenter.onProcess(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void read() {
        while (isConnected) {
            try {
                Log.i(TAG, "reciveMsg()...start recive messages");
                byte[] buffer = new byte[1024 * 4];
                int len = dis.read(buffer);
                if (len != -1) {
                    Depacket(buffer);
                }
            }catch (InvalidProtocolBufferException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d(TAG, e1.toString());
            }
        }
    }

    public boolean writeToSrv(DataFrame.Msg msg) {
        if (dos != null) {
            try {
                byte[] data = Enpacket(msg);
                dos.write(data);
                dos.flush();
                Log.i(TAG, "write to server:" + msg);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, e.toString());
                return false;
            }
        }
        Log.i(TAG, "目前没有连接上服务器");
        return false;
    }

    /**
     *  通讯协议
     *   +-----------------------------------------------+
     *   | Header(6) | MsgLength(4) | recive message.... |
     *   +-----------------------------------------------+
     *
     * @param databuf
     * @return
     */
    public byte[] Depacket(byte[] databuf) {
        int len = databuf.length;
        int i=0;
        for(; i < len; i++){
            //判断长度是否小于 Header+MsgLenth 的长度，小于则没有接收到实际信息
            if(len < i + ConstHeaderLength ){
                break;
            }
            //判断包头是否等于Header
            String header = new String(databuf,i,ConstHeaderLength);
            if(header.equals(ConstHeader)){
                //获取到MsgLenth中表示的信息长度
                ByteBuffer buffer = ByteBuffer.wrap(databuf,i+ConstHeaderLength,ConstMsgLength);
                int msgLenth = buffer.getInt();
                //判断实际接收信息长度是否等于表示信息长度
                if (len<i+ConstHeaderLength+ConstMsgLength+msgLenth){
                    break;
                }
                //截取msgLenth长的字节，即所要解析的信息
                byte[] data = Arrays.copyOfRange(databuf,i+ConstHeaderLength+ConstMsgLength,
                        i+ConstHeaderLength+ConstMsgLength+msgLenth);
                try {
                    recive_msg = DataFrame.Msg.parseFrom(data);
                    dataQueue.put(recive_msg);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //从当前的位置开始，再判断解析一次
                i = i+ConstHeaderLength+ConstMsgLength+msgLenth-1;
            }
        }
        if(i==len){
            return new byte[0];
        }
        return Arrays.copyOfRange(databuf,i,len);
    }

    //封包
    public byte[] Enpacket(DataFrame.Msg msg) {
        byte[] dataBuf = msg.toByteArray();
        int dataLen = dataBuf.length;
        ByteBuffer buffer = ByteBuffer.allocate(ConstHeaderLength+ ConstMsgLength + dataLen);
        buffer.put(ConstHeader.getBytes());
        buffer.putInt(dataLen);
        buffer.put(dataBuf);
        return buffer.array();
    }


    public void addIPresenter(IPresenter presenter) {
        Log.i(TAG, presenter + "插入");
        mPresenterList.add(presenter);
    }

    //程序退出时的清理工作
    public void setOnWork(boolean flag) {
        isConnected = flag;
    }

    public void setInstanceNull() {
        instance = null;
    }
}
