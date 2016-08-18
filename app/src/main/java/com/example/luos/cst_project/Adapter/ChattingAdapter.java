package com.example.luos.cst_project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.ChatMessage;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.TimeUtil;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by luos on 2016/8/9.
 */

public class ChattingAdapter extends BaseAdapter {
    protected static final String TAG = "Test_ChattingAdapter";
    private Context context;
    private List<ChatMessage> chatMessages;

    public ChattingAdapter(Context context, List<ChatMessage> messages) {
        super();
        this.context = context;
        this.chatMessages = messages;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        ChatMessage message = chatMessages.get(position);
        Log.i(TAG, message.toString());
        int direction = message.getDirection();   //direction: to or from ?
        int type = message.getType();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.msg_item, null);
            holder.leftLayout = (LinearLayout) convertView.findViewById(R.id.left_layout);
            holder.rightLayout = (LinearLayout) convertView.findViewById(R.id.right_layout);
            holder.leftChattingTime = (TextView) convertView.findViewById(R.id
                    .left_chatting_time_tv);
            holder.rightChattingTime = (TextView) convertView.findViewById(R.id
                    .right_chatting_time_tv);
            holder.leftChattingContent = (TextView) convertView.findViewById(R.id
                    .left_chatting_content_itv);
            holder.rightChattingConent = (TextView) convertView.findViewById(R.id
                    .right_chatting_content_itv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (direction == Config.MESSAGE_FROM) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            String time = message.getTime();
            if (time != null && !"".equals(time)) {
                String relativeTime = TimeUtil.getRelativeTime(time);
                Log.d(TAG, "relativeTime=" + relativeTime);
                holder.leftChattingTime.setText(relativeTime);
            }
            switch (type) {
                case Config.MESSAGE_TYPE_TXT:    //处理"文本"
                    String content = message.getContent();
                    Log.i(TAG, "getView():MESAGE_TYPE_TXT, content=" + content);
                    holder.leftChattingContent.setText(content);
                    break;
                default:
                    break;
            }
        } else if (direction == Config.MESSAGE_TO) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            String time = message.getTime();
            if (time != null && !"".equals(time)) {
                String relativeTime = TimeUtil.getRelativeTime(time);
                Log.d(TAG, "relativeTime=" + relativeTime);
                holder.rightChattingTime.setText(relativeTime);
            }
            switch (type) {
                case Config.MESSAGE_TYPE_TXT:    //处理"文本"
                    String content = message.getContent();
                    Log.i(TAG, "getView():MESAGE_TYPE_TXT, content=" + content);
                    holder.rightChattingConent.setText(content);
                    break;
                default:
                    break;
            }

        }

        return convertView;
    }

    // 优化listview的Adapter
    static class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftChattingTime;
        TextView rightChattingTime;
        TextView leftChattingContent;
        TextView rightChattingConent;
    }
}
