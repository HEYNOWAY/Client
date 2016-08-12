package com.example.luos.cst_project.Presenter;

/**
 * Created by luos on 2016/8/9.
 */

public interface IChatPresenter {
    boolean sendChatMessage(int sendId, int receiveId, String content, String time);
}
