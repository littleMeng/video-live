package com.example.meng.videolive.bean;

/**
 * Created by mengshen on 2016/11/1.
 */

public class GsonDouyuRoom {
    private int error;
    private Data data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int room_id;
        private String rtmp_url;
        private String rtmp_live;

        public String getRtmp_live() {
            return rtmp_live;
        }

        public String getRtmp_url() {
            return rtmp_url;
        }

        public int getRoom_id() {
            return room_id;
        }

        @Override
        public String toString() {
            return "GsonDouyuRoom [room_id=" + room_id + ", rtmp_url=" + rtmp_url + ", rtmp_live="
                    + rtmp_live + "]";
        }
    }
}
