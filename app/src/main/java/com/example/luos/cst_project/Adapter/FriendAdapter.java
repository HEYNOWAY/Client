package com.example.luos.cst_project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.Friend;
import com.example.luos.cst_project.R;
import com.example.luos.cst_project.Util.TimeUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by luos on 2016/7/10.
 */

public class FriendAdapter extends BaseAdapter {
    final static String TAG="FriendAdapter";
    private ArrayList<DataFrame.User> list;
    private Context context;
    private LayoutInflater inflater;
    private LinearLayout layout;

    public FriendAdapter(Context context, ArrayList<DataFrame.User> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.from(context).inflate(R.layout.friend_lsit_item, null);
//            holder.headImg=(ImageView)convertView.findViewById(R.id.head);
//            holder.headImg.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.content=(TextView)convertView.findViewById(R.id.content);
            holder.time=(TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        DataFrame.User friend=list.get(position);
        holder.name.setText(friend.getNickName());

//        int type=friend.getType();
//        if(type== Config.MESSAGE_TYPE_TXT){
//            holder.content.setText(friend.getContent());
//        }else if(type==Config.MESSAGE_TYPE_IMG){
//            holder.content.setText("[图片]");
//        }else if(type==Config.MESSAGE_TYPE_AUDIO){
//            holder.content.setText("[语音]");
//        }
//        String time=friend.getTime();
//        if(time!=null && !"".equals(time)){
//            holder.time.setText(TimeUtil.getRelativeTime(time));
//        }

        convertView.setTag(holder);
        return convertView;
    }

    static class ViewHolder{
        TextView name;        //昵称
        TextView content;	  //最后一条聊天消息的描述(文本、表情、图片、语音)
        TextView time;		  //最后一次聊天的时间
    }
}
