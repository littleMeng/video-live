package com.example.meng.videolive.bean;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class RoomInfo {
    private int roomId;
    private String roomSrc;
    private String roomName;
    private int online;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomSrc() {
        return roomSrc;
    }

    public void setRoomSrc(String roomSrc) {
        this.roomSrc = roomSrc;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
