package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
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
    private List<ChatMessage> msgList = new ArrayList<ChatMessage>();
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
        mEditText = (EditText) findViewById(R.id.text_editor);
        mSend = (Button) findViewById(R.id.send_button);
        mlistView = (ListView) findViewById(R.id.chatting_history_lv);
        mNickName = (TextView) findViewById(R.id.nickName);

        Intent intent = getIntent();
        Friend friend = intent.getParcelableExtra(FriendListFragment.EXTRA_FRIEND);
        Log.i(TAG,"get friend is "+friend);
        friendNickName = friend.getFriendName();
        friendID = friend.getFriendID();
        setAdapterForThis();

        iChatPresenter = new IChatPresenterCompl(this);
        mNickName.setText(friendNickName);
        mSend.setOnClickListener(this);

        registerForContextMenu(mlistView);

    }

    private void setAdapterForThis(){
        initMessages();
        if(msgList==null){
            msgList = new ArrayList<ChatMessage>();
        }
        adapter = new ChattingAdapter(this,msgList);
        mlistView.setAdapter(adapter);
    }

    public void initMessages(){
        Log.i(TAG,"initMessages()...selfId is:"+self.getUserID()+", friendId is:"+friendID);
        msgList = dbUtil.queryMessages(self.getUserID()+"", friendID+"");
        Log.i(TAG, "messages="+msgList);
        if(msgList!=null){
            Log.i(TAG, "initMessages() messages.size="+msgList.size());
        }
    }


    @Override
    public void processMessage(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:
                String str = mEditText.getText().toString().trim();
                String sendStr = str.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "").replaceAll("\f", "");
                if(str!=null&&!str.equals("")&&sendStr!=null){
                    sendChatMsg(Config.MESSAGE_TYPE_TXT,sendStr);
                    mEditText.setText("");
                } else {
                    makeTextShort("发送消息不能为空");
                }

        }
    }

    @Override
    public boolean sendChatMsg(int type, String content) {
        Log.i(TAG,"sendChatMsg()...");
        String time = TimeUtil.getAbsoluteTime();
        int userId = self.getUserID();
        boolean result = iChatPresenter.sendChatMessage(userId,friendID,content,time);
        if(result==true){
            ChatMessage message = new ChatMessage(userId,friendID,time,content,type,Config.MESSAGE_TO);
            msgList.add(message);
            Log.i(TAG,"send message is:"+message);
            iChatPresenter.saveMessageToDb(message);
            adapter.notifyDataSetChanged();
            return true;
        } else {
            makeTextShort("消息发送失败");
            return false;
        }
    }
}
