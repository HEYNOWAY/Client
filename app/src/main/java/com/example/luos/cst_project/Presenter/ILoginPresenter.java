package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.DataFrame;

import java.util.List;

/**
 * Created by luos on 2016/6/25.
 */

public interface ILoginPresenter extends IPresenter {
    void doLogin(String username, String password);

    void getOffMsg(int userId);

    void saveMessageToDb(List<DataFrame.PersonalMsg> msgList);
}
