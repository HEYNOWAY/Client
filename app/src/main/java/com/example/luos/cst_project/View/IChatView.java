package com.example.luos.cst_project.View;

import com.example.luos.cst_project.Model.ChatMessage;

import java.util.List;

/**
 * Created by luos on 2016/8/9.
 */

public interface IChatView {
    void clearText();
    void makeToast(String text);
    void msgListAddMsg(ChatMessage message);
    void notifyAdapterDataChange();

}
