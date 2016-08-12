package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Util.NetWork;
import com.example.luos.cst_project.View.ChatActivity;
import com.example.luos.cst_project.View.IChatView;

/**
 * Created by luos on 2016/8/9.
 */

public class IChatPresenterCompl implements IChatPresenter{
    private ChatActivity chatActivity;

    public IChatPresenterCompl(ChatActivity chatActivity){
        this.chatActivity = chatActivity;
    }

    @Override
    public boolean sendChatMessage(int sendId, int receiveId, String content, String time) {
        return NetWork.getInstance().sendText(sendId, receiveId,content,time);
    }
}
