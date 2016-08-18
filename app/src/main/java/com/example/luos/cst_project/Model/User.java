package com.example.luos.cst_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luos on 2016/6/30.
 */

public class User implements Parcelable {
    private int userID;
    private String userName;
    private String userPassword;
    private String nickName;


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(userName);
        dest.writeString(userPassword);
        dest.writeString(nickName);

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.userID = source.readInt();
            user.userName = source.readString();
            user.userPassword = source.readString();
            user.nickName = source.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
