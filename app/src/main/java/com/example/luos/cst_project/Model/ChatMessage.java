package com.example.luos.cst_project.Model;

/**
 * Created by luos on 2016/8/9.
 */

public class ChatMessage {

    private int receiveId;
    private int sendId;
    private int type;
    private int direction;
    private String time;
    private String content;

    public ChatMessage() {

    }

    public ChatMessage(int sendId, int receiveId, String time, String content, int type, int
            direction) {
        this.receiveId = receiveId;
        this.sendId = sendId;
        this.time = time;
        this.content = content;
        this.type = type;
        this.direction = direction;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "From: " + sendId +
                "\nSend to " + receiveId +
                "\nType:" + type +
                "\n Content:" + content +
                "\n Time:" + time +
                "\n Direction:" + direction;
    }
}
