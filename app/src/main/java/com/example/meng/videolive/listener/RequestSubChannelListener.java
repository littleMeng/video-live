package com.example.meng.videolive.listener;

import com.example.meng.videolive.bean.RoomInfo;

import java.util.List;

/**
 * Created by uspai.taobao.com on 2016/7/5.
 */
public interface RequestSubChannelListener {
    void onSuccess(List<RoomInfo> roomInfos);

    void onError();
}
