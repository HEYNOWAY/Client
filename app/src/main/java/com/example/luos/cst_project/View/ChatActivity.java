package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.luos.cst_project.Adapter.ChattingAdapter;
import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.ChatMessage;
import com.example.luos.cst_project.Model.Friend;
import com.example.luos.cst_project.Presenter.IChatPresenter;
import com.example.luos.cst_project.Presenter.IChatPresenterCompl;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/8/8.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener ,IChatView{
    protected static final String TAG = "Test_ChatActivity";
    private List<ChatMessage> msgList;
    private ListView mlistView;
    private EditText mEditText;
    private Button mSend;
    private TextView mNickName;
    private ChattingAdapter adapter;
    private IChatPresenter iChatPresenter;
    private int friendID;
    private String friendNickName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate()...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        Init();
    }


    private void Init() {
        Log.i(TAG,"onInit()...");
        //初始化控件件
        mEditText = (EditText) findViewById(R.id.text_editor);
        mSend = (Button) findViewById(R.id.send_button);
        mlistView = (ListView) findViewById(R.id.chatting_history_lv);
        mNickName = (TextView) findViewById(R.id.nickName);

        //获取Intent数据
        Intent intent = getIntent();
        Friend friend = intent.getParcelableExtra(FriendListFragment.EXTRA_FRIEND);
        friendNickName = friend.getFriendName();
        friendID = friend.getFriendID();

        //初始化设置
        setNickName();
        setPresneter();
        setAdapterForThis();

        mSend.setOnClickListener(this);
        registerForContextMenu(mlistView);
    }

    private void setNickName(){
        mNickName.setText(friendNickName);
    }

    private void setPresneter(){
        iChatPresenter = new IChatPresenterCompl(this);
    }

    private void setAdapterForThis(){
        msgList = dbUtil.queryMessages(self.getUserID()+"", friendID+"");
        if(msgList==null){
            msgList = new ArrayList<>();
        }
        adapter = new ChattingAdapter(this,msgList);
        mlistView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:
                String text = mEditText.getText().toString();
                iChatPresenter.doSendMessage(text,friendID);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("删除聊天记录");
        menu.add(0, 0, 0, "删除全部聊天记录？");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        dbUtil.deleteAllMessages(self.getUserID()+"", friendID+"");
        setAdapterForThis();
        return super.onContextItemSelected(item);
    }

    @Override
    public void processMessage(Message msg) {

    }

    @Override
    public void clearText() {
        mEditText.setText("");
    }

    @Override
    public void makeToast(String text) {
        makeTextShort(text);
    }

    @Override
    public void msgListAddMsg(ChatMessage message) {
        msgList.add(message);
    }

    @Override
    public void notifyAdapterDataChange() {
        adapter.notifyDataSetChanged();
    }
}
