package com.example.meng.videolive.bean;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class BuildUrl {
    public static final String DOUYU_API = "http://capi.douyucdn.cn/api/v1/";

    public static String getDouyuDota2SubChannel() {
        return DOUYU_API + "live/3?&limit=20&offset=0";
    }

    public static String getDouyuDota2Room(int roomId) {
        long time = System.currentTimeMillis()/1000;
        String commmon = "room/" + roomId + "?aid=android&cdn=ws&client_sys=android&time=" + time;
        String auth = Md5.strToMd5Low32(commmon + "1231");
        return DOUYU_API + commmon + "&auth=" + auth;
    }
}
