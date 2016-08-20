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
    public static final String EXTRA_FRIEND= "friend";
    private static final String TAG = "FriendListFragment";
    private ArrayList<Friend> list;
    private FriendAdapter adapter;

    public FriendListFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("我的朋友");
        Log.i(TAG,"onCreate...");
//        registerForContextMenu(getListView());
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume()...");
        initData();
    }

    public void initData() {
        Log.i(TAG,"initData()...");
        User self = BaseIPresenter.getUser();
        DbUtil dbUtil = BaseIPresenter.getDbUtil();
        if(list==null){
            Log.i(TAG, "initData self="+ self +" dbUtil="+ dbUtil);
            list=dbUtil.queryFriends(self.getUserID()+"");
            adapter=new FriendAdapter(getActivity(), list);
            setListAdapter(adapter);
        }else{
            list=dbUtil.queryFriends(self.getUserID()+"");
            Log.i(TAG, "回到FriendListActivity,通知数据更新; list.size="+list.size());
            adapter=new FriendAdapter(getActivity(), list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG,"onListItemClick()...");
        Friend friend = (Friend) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra(EXTRA_FRIEND,friend);
        Log.i(TAG,"friend Id is:"+friend.getFriendID()+ " friend nickName is:"+friend.getFriendName());
        startActivity(i);
    }

    public FriendAdapter getAdapter(){
        return adapter;
    }



}
