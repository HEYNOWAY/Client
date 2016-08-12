package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.luos.cst_project.Adapter.MsgListAdapter;
import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.MsgData;
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
    private List<MsgData> msgList = new ArrayList<MsgData>();
    private ListView mlistView;
    private EditText mEditText;
    private Button mSend;
    private MsgListAdapter adapter;
    private IChatPresenter iChatPresenter;
    private int friendID;
    private String friendNickName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        Init();
    }

    private void Init() {
        mEditText = (EditText) findViewById(R.id.input_msg);
        mSend = (Button) findViewById(R.id.sned_btn);
        mlistView = (ListView) findViewById(R.id.msg_list_view);
        adapter = new MsgListAdapter(this,R.layout.msg_item,msgList);
        mlistView.setAdapter(adapter);
        mSend.setOnClickListener(this);
        iChatPresenter = new IChatPresenterCompl(this);
        Intent intent = getIntent();
        friendID = intent.getIntExtra(FriendListFragment.EXTRA_FRIENDID,0);
        friendNickName = intent.getStringExtra(FriendListFragment.EXTRA_FRIENDNICKNAME);
    }

    @Override
    public void processMessage(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sned_btn:
                String str = mEditText.getText().toString().trim();
                String sendStr = str.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "").replaceAll("\f", "");
                if(str!=null&&!str.equals("")&&sendStr!=null){
                    sendChatMsg(Config.MESSAGE_TO,sendStr);
                    mEditText.setText("");
                } else {
                    makeTextShort("发送消息不能为空");
                }

        }
    }

    @Override
    public void sendChatMsg(int type, String content) {
        Log.d("Test_sendChatMsg","sendChatmsg()...");
        String time = TimeUtil.getAbsoluteTime();
        int userId = self.getUserID();
        boolean result = iChatPresenter.sendChatMessage(userId,friendID,content,time);
        if(result==true){
            msgList.add(new MsgData(self.getUserID(),friendID,time,content,type));
            adapter.notifyDataSetChanged();
        } else {
            makeTextShort("消息发送失败");
        }
    }
}
