package com.example.meng.videolive.bean;

/**
 * Created by 小萌神_0 on 2016/5/28.
 */
public class GsonDouyuRoomInfo {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int room_id;
        private String room_src;
        private String room_name;
        private String nickname;
        private int online;
        private String rtmp_url;
        private String rtmp_live;
        private String hls_url;

        public int getRoom_id() {
            return room_id;
        }

        public String getRoom_src() {
            return room_src;
        }

        public int getOnline() {
            return online;
        }

        public String getRoom_name() {
            return room_name;
        }

        public String getNickname() {
            return nickname;
        }

        public String getRtmp_url() {
            return rtmp_url;
        }

        public String getRtmp_live() {
            return rtmp_live;
        }

        public String getHls_url() {
            return hls_url;
        }

        @Override
        public String toString() {
            return "GsonDouyuRoomInfo [room_id" + room_id + ", room_src" + room_src +
                    ", room_name" + room_name + ", online" + online + ", rtmp_url" + rtmp_url
                    + ", rtmp_live" + rtmp_live + ", hls_url" + hls_url + "]";
        }
    }
}
