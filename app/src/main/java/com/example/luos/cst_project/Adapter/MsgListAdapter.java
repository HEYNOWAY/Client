package com.example.luos.cst_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.MsgData;
import com.example.luos.cst_project.R;

import java.util.List;

/**
 * Created by luos on 2016/8/9.
 */

public class MsgListAdapter extends ArrayAdapter<MsgData> {
    private int resourceId;

    public MsgListAdapter(Context context, int resource, List<MsgData> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgData msg = getItem(position);
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) convertView.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) convertView.findViewById(R.id.right_layout);
            viewHolder.leftText = (TextView) convertView.findViewById(R.id.left_msg);
            viewHolder.rightText = (TextView) convertView.findViewById(R.id.right_msg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(msg.getType()== Config.MESSAGE_FROM){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.rightText.setText(msg.getContent());
        } else if(msg.getType()==Config.MESSAGE_TO){
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightText.setText(msg.getContent());
        }
        return convertView;

    }

    class ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftText;
        TextView rightText;
    }
}
