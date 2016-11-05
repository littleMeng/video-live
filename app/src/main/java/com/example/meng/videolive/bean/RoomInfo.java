package com.example.meng.videolive.bean;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class RoomInfo {
    private int roomId;
    private String roomSrc;
    private String roomName;

    private String nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "RoomInfo [roomId=" + roomId + ", roomSrc=" + roomSrc + ", roomName=" + roomName
                + ", nickname=" + nickname + ", online=" + online + "]";
    }
}
