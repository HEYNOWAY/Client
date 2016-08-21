package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/8/21.
 */

public interface IFriendListPresenter extends IPresenter {
    ArrayList<Friend> getFriendsFromDb();
    void saveMessageToDb(List<DataFrame.PersonalMsg> msgList);
}
