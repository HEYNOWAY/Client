package com.example.luos.cst_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {
	private int friendID;       //好友ID
	private String friendName;     //好友名字
	private String head;           //好友头像路径
	private String headModifyTime; //头像的时间戳
	private String sex;
	private int type;		     //最后一条消息类型
	private String content;      //最后一条信息的内容
	private String time;         //最后一条信息的时间
	
	public Friend(){
		
	}
	
	public Friend(Parcel in){
	    friendID=in.readInt();
	    friendName=in.readString();
	    head=in.readString();
	    headModifyTime=in.readString();
	    sex=in.readString();
	    type=in.readInt();
	    content=in.readString();
	    time=in.readString();
	}

	public String getHeadModifyTime() {
        return headModifyTime;
    }

    public void setHeadModifyTime(String headModifyTime) {
        this.headModifyTime = headModifyTime;
    }

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getFriendID() {
		return friendID;
	}

	public void setFriendID(int friendID) {
		this.friendID = friendID;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "friend Id is"+friendID;
	}

	@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(friendID);
        out.writeString(friendName);
        out.writeString(head);
        out.writeString(headModifyTime);
        out.writeString(sex);
        out.writeInt(type);
        out.writeString(content);
        out.writeString(time);
    }
    
    public static final Parcelable.Creator<Friend> CREATOR= new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
