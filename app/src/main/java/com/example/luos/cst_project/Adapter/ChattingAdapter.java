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
        ViewHolder holder = new ViewHolder();
        ChatMessage message = chatMessages.get(position);
        int direction=message.getDirection();   //direction: to or from ?
        int type = message.getType();
        if (direction== Config.MESSAGE_FROM) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_from, null);
        }else if(direction==Config.MESSAGE_TO) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_to, null);
        }

        holder.time=(TextView)convertView.findViewById(R.id.chatting_time_tv);
        holder.text = (TextView) convertView
                .findViewById(R.id.chatting_content_itv);

        String time=message.getTime();
		Log.d(TAG, "time="+time);
        if(time!=null && !"".equals(time)){
            String relativeTime= TimeUtil.getRelativeTime(time);
			Log.d(TAG, "relativeTime="+relativeTime);
            holder.time.setText(relativeTime);
        }

        switch (type) {
            case Config.MESSAGE_TYPE_TXT: {    //处理"文本"
                String content=message.getContent();
                Log.i(TAG, "getView():MESAGE_TYPE_TXT, content="+content);
                if(direction==Config.MESSAGE_FROM){
                    holder.text.setText(content);
                } else {
                    holder.text.setText(content);
                }

                return convertView;
            }
            case Config.MESSAGE_TYPE_IMG: {   //处理 “图片”
                String filePath = message.getContent();
                try {
                    Bitmap bitmap = getThumbNail(filePath, 100);
                    holder.img.setLayoutParams(new LinearLayout.LayoutParams(100, 80));
                    holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
                    holder.img.setImageBitmap(bitmap);

                    holder.text.setText("");
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "发生异常："+e.toString());
                }
            }
            break;

        }

        return convertView;
    }

    /**
     * 获取uri指定的图片的缩略图
     * @param uri   指向图片的URI
     * @param cr    内容解决者
     * @param width 图片想要显示的宽度（像素）
     * @return
     * @throws FileNotFoundException
     */
    private Bitmap getThumbNail(String filePath, int width)
            throws FileNotFoundException {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath, options);
        int be=(int)(options.outWidth/(float)width);
        options.inSampleSize=be;
        options.inJustDecodeBounds=false;
        Bitmap bitmap=BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    // 优化listview的Adapter
    static class ViewHolder {
        TextView time;
        TextView text;
        ImageView img;
    }
}
