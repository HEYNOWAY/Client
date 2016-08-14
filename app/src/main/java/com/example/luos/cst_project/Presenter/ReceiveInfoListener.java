package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.ChatMessage;

/**
 * Created by luos on 2016/8/12.
 */

public interface ReceiveInfoListener {

    public boolean receive(ChatMessage info);
}
