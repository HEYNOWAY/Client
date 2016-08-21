package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.ChatMessage;

/**
 * Created by luos on 2016/8/9.
 */

public interface IChatPresenter extends IPresenter {

    boolean sendChatMessage(int receiveId, String content, String time);

    void saveMessageToDb(ChatMessage message);
}
