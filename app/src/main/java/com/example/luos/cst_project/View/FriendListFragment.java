package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luos.cst_project.Adapter.FriendAdapter;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.Friend;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Presenter.BaseIPresenter;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.DbUtil;

import java.util.ArrayList;

/**
 * Created by luos on 2016/7/27.
 */

public class FriendListFragment extends ListFragment {
    public static final String EXTRA_FRIENDID = "friend_id";
    public static final String EXTRA_FRIENDNICKNAME = "friend_nickName";
    public static final String EXTRA_INDEX = "index";
    private ArrayList<Friend> list;
    private FriendAdapter adapter;

    public FriendListFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("我的朋友");
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        User self = BaseIPresenter.getUser();
        DbUtil dbUtil = BaseIPresenter.getDbUtil();
        if(list==null){
            Log.i("Test_FriendListFragment", "initData self="+ self +" dbUtil="+ dbUtil);
            list=dbUtil.queryFriends(self.getUserID()+"");
            adapter=new FriendAdapter(getActivity(), list);
            setListAdapter(adapter);
        }else{
            list=dbUtil.queryFriends(self.getUserID()+"");
            Log.i("Tet_FreendListFragment", "回到FriendListActivity,通知数据更新; list.size="+list.size());
            adapter=new FriendAdapter(getActivity(), list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Friend friend = (Friend) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra(EXTRA_FRIENDNICKNAME,friend.getFriendName());
        Log.d("Test onListItemClick()",friend.toString());
        startActivity(i);
    }



}
