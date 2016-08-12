package com.example.luos.cst_project.Model;

/**
 * Created by luos on 2016/7/27.
 */

public class Friend {
    private int friendID;
    private String frinedName;
    private String friendNickName;
    private String sex;
    private String content;
    private String time;

    public Friend(){

    }

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

    public String getFrinedName() {
        return frinedName;
    }

    public void setFrinedName(String frinedName) {
        this.frinedName = frinedName;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
}
