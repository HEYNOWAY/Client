package com.example.luos.cst_project.Presenter;

import android.content.ContentValues;
import android.util.Log;

import com.example.luos.cst_project.Model.ChatMessage;
import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.Util.TimeUtil;
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.IChatView;

/**
 * Created by luos on 2016/8/9.
 */


public class IChatPresenterCompl  implements IChatPresenter {
    private static final String TAG = "IChatPresenterCompl";
    private DataFrame.PersonalMsg.Builder personalBuilder = DataFrame.PersonalMsg.newBuilder();
    private IChatView iChatView;
    private DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    private NetWork netWork = NetWork.getInstance();
    private int userId = BaseActivity.getSelf().getUserID();

    public IChatPresenterCompl(IChatView iChatView) {
        this.iChatView = iChatView;
    }


    public void doSendMessage(String text, int friendID){
        String str = text.trim();
        String sendStr = str.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "").replaceAll("\f", "");
        if(!str.equals("") && sendStr!=null){
            sendMsgAndSaveToDb(Config.MESSAGE_TYPE_TXT,sendStr,friendID);
            iChatView.clearText();
        } else {
            iChatView.makeToast("发送消息不能为空");
        }
    }

    private boolean sendMsgAndSaveToDb(int type, String content, int friendID) {
        Log.i(TAG,"sendChatMsg()...");
        String time = TimeUtil.getAbsoluteTime();

        DataFrame.Msg send_msg = msgBuilder.setUserOpt(Config.REQUEST_SEND_TXT)
                .addPersonalMsg(
                        personalBuilder.setSenderID(userId)
                                .setRecverID(friendID)
                                .setContent(content)
                                .setSendTime(time)).build();
        boolean result = netWork.writeToSrv(send_msg);

        if(result){
            ChatMessage message = new ChatMessage(userId,friendID,time,content,type,Config.MESSAGE_TO);
            Log.i(TAG,"send message is:"+message);
            saveMessageToDb(message);
            iChatView.msgListAddMsg(message);
            iChatView.notifyAdapterDataChange();
            return true;
        } else {
            iChatView.makeToast("消息发送失败");
            return false;
        }
    }

    private void saveMessageToDb(ChatMessage message) {
        ContentValues values=new ContentValues();
        values.put(MsgEntry.SEND_ID, message.getSendId());
        values.put(MsgEntry.RECEVICE_ID, message.getReceiveId());
        values.put(MsgEntry.DIRECTION, message.getDirection());
        values.put(MsgEntry.TYPE, message.getType());
        values.put(MsgEntry.CONTENT, message.getContent());
        values.put(MsgEntry.TIME, message.getTime());
        BaseActivity.getDbUtil().insertMessage(values);
    }

    @Override
    public void onProcess(DataFrame.Msg msg) {

    }
}
