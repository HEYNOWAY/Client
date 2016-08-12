package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Presenter.IFriendListPresenterCompl;
import com.example.luos.cst_project.R;

import java.util.ArrayList;

public class FriendListActivity extends BaseActivity implements IFriendListView {
    private FriendListFragment fragment;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Intent i = getIntent();
        user = i.getParcelableExtra(LoginActivity.USERDATA);

        FragmentManager fm = getSupportFragmentManager();
        if(friends!=null){
            Log.d("Test_FriendListActivity",friends.toString());
        }
        fragment = new FriendListFragment();
        fm.beginTransaction()
                .add(R.id.activity_friend_list,fragment)
                .commit();
        fragment.setFriends(friends);
        IFriendListPresenterCompl compl = new IFriendListPresenterCompl(this);
    }

    @Override
    public void processMessage(Message msg) {
        Log.d("Test_processMessage","FriendsList procemessage()...");
        switch (msg.what){

        }
    }

    @Override
    public ArrayList<DataFrame.User> getFriendList() {
        return friends;
    }

    @Override
    public void setFriendList(ArrayList<DataFrame.User> friends) {
        this.friends = friends;
    }
}
