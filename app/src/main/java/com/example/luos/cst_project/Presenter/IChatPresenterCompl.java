package com.example.luos.cst_project.Presenter;

import android.content.ContentValues;

import com.example.luos.cst_project.Model.ChatMessage;
import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Util.MsgDbContract.MsgEntry;
import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.BaseActivity;
import com.example.luos.cst_project.View.IChatView;

/**
 * Created by luos on 2016/8/9.
 */

public class IChatPresenterCompl  implements IChatPresenter {
    private DataFrame.PersonalMsg.Builder personalBuilder = DataFrame.PersonalMsg.newBuilder();
    private IChatView iChatView;
    DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    private NetWork netWork = NetWork.getInstance();

    public IChatPresenterCompl(IChatView iChatView) {
        this.iChatView = iChatView;
        netWork.addIPresenter(this);
    }

    @Override
    public boolean sendChatMessage( int receiveId, String content, String time) {
        DataFrame.Msg send_msg = msgBuilder.setUserOpt(Config.REQUEST_SEND_TXT)
                .addPersonalMsg(
                        personalBuilder.setSenderID(BaseActivity.self.getUserID())
                                .setRecverID(receiveId)
                                .setContent(content)
                                .setSendTime(time)).build();
        boolean result = netWork.writeToSrv(send_msg);
        return result;
    }

    public void saveMessageToDb(ChatMessage message) {
        ContentValues values = new ContentValues();
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
