package com.example.meng.videolive.bean;

import java.util.List;

/**
 * Created by uspai.taobao.com on 2016/6/24.
 */
public class GsonAllSubChannels {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data {
        private int tag_id;
        private String tag_name;
        private String icon_url;

        public int getTag_id() {
            return tag_id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public String getIcon_url() {
            return icon_url;
        }

        @Override
        public String toString() {
            return "Data [tag_id=" + tag_id + ", tag_name="
                    + tag_name + ", icon_url=" + icon_url + "]";
        }
    }

    @Override
    public String toString() {
        return "GsonAllSubChannels [data=" + data + "]";
    }
}
