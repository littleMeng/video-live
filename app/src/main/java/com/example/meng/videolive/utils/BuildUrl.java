package com.example.meng.videolive.utils;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class BuildUrl {
    public static final String DOUYU_API = "http://capi.douyucdn.cn/api/v1/";

    public static String getDouyuLOLSubChannel() {
        return DOUYU_API + "live/1?&limit=20&offset=0";
    }

    public static String getDouyuFurnaceStoneSubChannel() {
        return DOUYU_API + "live/2?&limit=20&offset=0";
    }

    public static String getDouyuDota2SubChannel() {
        return DOUYU_API + "live/3?&limit=20&offset=0";
    }

    public static String getDouyuAllSubChannels() {
        return DOUYU_API + "getColumnDetail?shortName=game";
    }

    public static String getDouyuLiveChannel() {
        return DOUYU_API + "live?&limit=30&offset=0";
    }

    public static String getDouyuSubChannelBaseTag(int channelTag) {
        return DOUYU_API + "live/" + channelTag + "?&limit=20&offset=0";
    }

    public static String getDouyuRoom(int roomId) {
        long time = System.currentTimeMillis()/1000;
        String commmon = "room/" + roomId + "?aid=android&cdn=ws&client_sys=android&time=" + time;
        String auth = Md5.strToMd5Low32(commmon + "1231");
        return DOUYU_API + commmon + "&auth=" + auth;
    }
}
