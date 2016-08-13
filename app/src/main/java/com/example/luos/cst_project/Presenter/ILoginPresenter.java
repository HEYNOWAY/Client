package com.example.luos.cst_project.Presenter;

/**
 * Created by luos on 2016/6/25.
 */

public interface ILoginPresenter {
    void doLogin(String username,String password);
    void setProgressBarVisible(int visibility);
    void getOffMsg(int userId);
}
