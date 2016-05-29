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
        private String rtmp_url;
        private String rtmp_live;
        private String hls_url;

        public String getRtmp_url() {
            return rtmp_url;
        }

        public void setRtmp_url(String rtmp_url) {
            this.rtmp_url = rtmp_url;
        }

        public String getRtmp_live() {
            return rtmp_live;
        }

        public void setRtmp_live(String rtmp_live) {
            this.rtmp_live = rtmp_live;
        }

        public String getHls_url() {
            return hls_url;
        }

        public void setHls_url(String hls_url) {
            this.hls_url = hls_url;
        }

        @Override
        public String toString() {
            return "GsonDouyuRoomInfo [rtmp_url" + rtmp_url + ", rtmp_live" + rtmp_live +
                    ", hls_url" + hls_url + "]";
        }
    }
}
