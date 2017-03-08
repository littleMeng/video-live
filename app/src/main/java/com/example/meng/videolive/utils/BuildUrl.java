package com.example.meng.videolive.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class BuildUrl {
    public static final String DOUYU_API = "http://capi.douyucdn.cn/api/v1/";

    public static String getDouyuLOLSubChannel() {
        return DOUYU_API + "live/1?&limit=20";
    }

    public static String getDouyuFurnaceStoneSubChannel() {
        return DOUYU_API + "live/2?&limit=20";
    }

    public static String getDouyuDota2SubChannel() {
        return DOUYU_API + "live/3?&limit=20";
    }

    public static String getDouyuAllSubChannels() {
        return DOUYU_API + "getColumnDetail?shortName=game";
    }

    public static String getDouyuLiveChannel() {
        return DOUYU_API + "live?&limit=20";
    }

    public static String getDouyuSubChannelBaseTag(int channelTag) {
        return DOUYU_API + "live/" + channelTag + "?&limit=20";
    }

    public static String getDouyuRoom(int roomId) {
        return "http://capi.douyucdn.cn/api/v1/searchNew/" + roomId + "/1?limit=1&offset=0";
    }

    public static String getDouyuRoomUrl(int roomId) {
        return "http://coapi.douyucdn.cn/lapi/live/thirdPart/getPlay/" + roomId + "?rate=0";
    }

    public static HashMap<String, String> getDouyuRoomParams(int roomId) {
        int time = (int) (System.currentTimeMillis()/1000);
        String signContent = "lapi/live/thirdPart/getPlay/" + roomId + "?aid=pcclient&rate=0&time="
                + time + "9TUk5fjjUjg9qIMH3sdnh";
        String sign = Md5.strToMd5Low32(signContent);

        HashMap<String, String> map = new HashMap<>();
        map.put("auth", sign);
        map.put("time", "" + time);
        map.put("aid", "pcclient");

        return map;
    }

    public static String getDouyuSearchUrl(String keyWord) {
        return "http://capi.douyucdn.cn/api/v1/mobileSearch/1/1?sk=" + keyWord +
                "&limit=10&client_sys=android";
    }
}
