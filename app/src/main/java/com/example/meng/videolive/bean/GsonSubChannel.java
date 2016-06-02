package com.example.meng.videolive.bean;

import java.util.List;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class GsonSubChannel {
    private List<Room> data;

    public List<Room> getData() {
        return data;
    }

    public void setData(List<Room> data) {
        this.data = data;
    }

    public static class Room {
        private int room_id;
        private String room_src;
        private String room_name;
        private String nickname;
        private int online;

        public int getRoom_id() {
            return room_id;
        }

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public String getRoom_src() {
            return room_src;
        }

        public void setRoom_src(String room_src) {
            this.room_src = room_src;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
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
            return "Room [room_id=" + room_id + ", room_src" + room_src +
                    ", room_name" + room_name + ", nickname" + nickname +
                    ", setOnline" + online + "]";
        }
    }

    @Override
    public String toString() {
        return "GsonSubChannel [data" + data + "]";
    }
}
