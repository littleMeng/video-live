package com.example.meng.videolive.bean;

import java.util.List;

/**
 * Created by 小萌神_0 on 2016/5/28.
 */
public class GsonDouyuRoomInfo {
    private int error;

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public static class Data {
        private List<GsonSubChannel.Room> room;

        public List<GsonSubChannel.Room> getRoom() {
            return room;
        }

//        private int room_id;
//        private String room_src;
//        private String room_name;
//        private String nickname;
//        private int online;
//
//        public int getRoom_id() {
//            return room_id;
//        }
//
//        public String getRoom_src() {
//            return room_src;
//        }
//
//        public int getOnline() {
//            return online;
//        }
//
//        public String getRoom_name() {
//            return room_name;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        @Override
//        public String toString() {
//            return "GsonDouyuRoomInfo [room_id=" + room_id + ", room_src=" + room_src +
//                    ", room_name=" + room_name + ", online=" + online + "]";
//        }
    }
}
