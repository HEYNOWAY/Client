package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Presenter.IFriendListPresenterCompl;
import com.example.luos.cst_project.R;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends BaseActivity implements IFriendListView {
    private static final String TAG = "FriendListActivity";
    private FriendListFragment fragment;
    private TextView userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate...");

        setContentView(R.layout.activity_friend_list);
        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(self.getNickName());
        FragmentManager fm = getSupportFragmentManager();
        fragment = new FriendListFragment();
        fm.beginTransaction()
                .add(R.id.activity_friend_list,fragment)
                .commit();
    }

    @Override
    public void processMessage(Message msg) {
        Log.i(TAG,"processMessage()...");
        switch (msg.what){
            case Config.SEND_NOTIFICATION:
                Log.i(TAG,"send notificition...");
                Bundle bundle = msg.getData();
                Log.i(TAG,"bundle getData is :"+bundle);
                ArrayList data = bundle.getParcelableArrayList("msgList");
                List<DataFrame.PersonalMsg> msgList = new ArrayList<>();
                if(data!=null){
                   msgList = (List<DataFrame.PersonalMsg>) data.get(0);
                }
                fragment.getPresenter().saveMessageToDb(msgList);
                fragment.initData();
                fragment.getAdapter().notifyDataSetChanged();
                sendNotifycation(msgList);
                break;
            default:
                break;

        }
    }
}
