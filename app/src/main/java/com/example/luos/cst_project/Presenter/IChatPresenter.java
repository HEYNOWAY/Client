package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.ChatMessage;

/**
 * Created by luos on 2016/8/9.
 */

public interface IChatPresenter extends IPresenter {
    void doSendMessage(String text, int friendID);
}
