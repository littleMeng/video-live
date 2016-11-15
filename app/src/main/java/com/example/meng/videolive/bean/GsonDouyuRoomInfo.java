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
    }
}
