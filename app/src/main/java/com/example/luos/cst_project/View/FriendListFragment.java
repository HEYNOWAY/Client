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
import com.example.luos.cst_project.Presenter.IFriendListPresenter;
import com.example.luos.cst_project.Presenter.IFriendListPresenterCompl;
import com.example.luos.cst_project.Presenter.IPresenter;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.DbUtil;

import java.util.ArrayList;

/**
 * Created by luos on 2016/7/27.
 */

public class FriendListFragment extends ListFragment implements IFriendListView {
    public static final String EXTRA_FRIEND = "friend";
    private static final String TAG = "FriendListFragment";
    private ArrayList<Friend> list;
    private FriendAdapter adapter;
    private IFriendListPresenter friendListPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("我的朋友");
        Log.i(TAG, "onCreate...");
        friendListPresenter = new IFriendListPresenterCompl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()...");
        initData();
    }

    public void initData() {
        Log.i(TAG, "initData()...");
        if (list == null) {
            list = friendListPresenter.getFriendsFromDb();
            adapter = new FriendAdapter(getActivity(), list);
            setListAdapter(adapter);
        } else {
            list = friendListPresenter.getFriendsFromDb();
            adapter = new FriendAdapter(getActivity(), list);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG, "onListItemClick()...");
        Friend friend = (Friend) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra(EXTRA_FRIEND, friend);
        startActivity(i);
    }

    public FriendAdapter getAdapter() {
        return adapter;
    }

    public IFriendListPresenter getPresenter(){
        return friendListPresenter;
    }


}
