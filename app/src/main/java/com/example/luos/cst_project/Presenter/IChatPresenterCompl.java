package com.example.luos.cst_project.Presenter;

import android.content.ContentValues;

import com.example.luos.cst_project.Model.ChatMessage;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.IChatView;

/**
 * Created by luos on 2016/8/9.
 */

public class IChatPresenterCompl extends BaseIPresenter implements IChatPresenter{
    private IChatView iChatView;

    public IChatPresenterCompl(IChatView iChatView){
        this.iChatView = iChatView;
    }

    @Override
    public boolean sendChatMessage(int sendId, int receiveId, String content, String time) {
        return NetWork.getInstance().sendText(sendId, receiveId,content,time);
    }

    public void saveMessageToDb(ChatMessage message) {
        ContentValues values=new ContentValues();
        values.put(MsgEntry.SEND_ID, "'"+message.getSendId()+"'");
        values.put(MsgEntry.RECEVICE_ID, "'"+message.getReceiveId()+"'");
        values.put(MsgEntry.DIRECTION, message.getDirection());
        values.put(MsgEntry.TYPE, message.getType());
        values.put(MsgEntry.CONTENT, message.getContent());
        values.put(MsgEntry.TIME, message.getTime());
        getDbUtil().insertMessage(values);
    }

}
