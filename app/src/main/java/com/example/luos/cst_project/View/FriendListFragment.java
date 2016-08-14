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
import com.example.luos.cst_project.R;

import java.util.ArrayList;

/**
 * Created by luos on 2016/7/27.
 */

public class FriendListFragment extends ListFragment {
    public static final String EXTRA_FRIENDID = "friend_id";
    public static final String EXTRA_FRIENDNICKNAME = "friend_nickName";
    public static final String EXTRA_INDEX = "index";
    private ArrayList<DataFrame.User> friends;

    public FriendListFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("我的朋友");
        FriendAdapter adapter = new FriendAdapter(getActivity(),friends);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DataFrame.User friend = (DataFrame.User) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra(EXTRA_FRIENDID,friend.getUserID());
        i.putExtra(EXTRA_FRIENDNICKNAME,friend.getNickName());
        Log.d("Test onListItemClick()",friend.toString());
        startActivity(i);
    }

    public void setFriends(ArrayList<DataFrame.User> friends){
        this.friends = friends;
    }

//    private class friendAdapter extends ArrayAdapter<DataFrame.User> {
//
//
//        public friendAdapter(ArrayList<DataFrame.User> friends) {
//            super(getActivity(), 0, friends);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView==null){
//                convertView = getActivity().getLayoutInflater()
//                        .inflate(R.layout.friend_lsit_item,null);
//            }
//            TextView textView = (TextView) convertView.findViewById(R.id.friend_name);
//            DataFrame.User friend = getItem(position);
//
//            textView.setText(friend.getNickName());
//            return convertView;
//        }
//    }
}
